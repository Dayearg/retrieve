package edu.njucm.retrieve.services.Impl;


import edu.njucm.retrieve.dao.ESRepository;
import edu.njucm.retrieve.model.DocumentES;
import edu.njucm.retrieve.model.DocumentFirstSearch;
import edu.njucm.retrieve.model.DocumentSecondSearch;
import edu.njucm.retrieve.services.ElasticsearchService;
import org.apache.lucene.search.Explanation;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    private ESRepository esRepository;


    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Override
    public boolean save(DocumentES documentES) {
        esRepository.save(documentES);
        return true;
    }

    @Override
    public boolean saveAll(List<DocumentES> documents) {
        esRepository.saveAll(documents);
        return true;
    }


    @Override
    public List<DocumentFirstSearch> firstSearch(BoolQueryBuilder boolQueryBuilder) throws IOException {

        /**
         * 创建了一个SearchSourceBuilder对象，用于构建查询请求，并创建了一个BoolQueryBuilder对象。
         * 它包含两个Match查询条件（title和text），这些查询条件用于搜索与给定文本匹配的文档。
         * 使用should方法将这两个Match查询条件组合为OR关系，表示只要有一个条件满足就可以匹配上。
         */
        List<DocumentFirstSearch> documentFirstSearchList = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        /*BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("title", title).operator(Operator.OR))
                .should(QueryBuilders.matchQuery("text", text).operator(Operator.OR));*/


        /**
         * 将BoolQueryBuilder对象设置为查询语句，即将其作为参数传递给SearchSourceBuilder对象的query方法。
         */
        searchSourceBuilder.query(boolQueryBuilder);


        /**
         * 使用TermsAggregationBuilder对象定义一个聚合，它将根据titleKeyword和uploadUser字段进行分组，同时计算每篇文章的关键词出现频率的总和。
         */
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders
                .terms("group_by_titleKeyword_uploadUser").field("titleKeyword").size(1000)
                .subAggregation(AggregationBuilders.terms("group_by_uploadUser").field("uploadUser").size(1000)).size(1000);

        searchSourceBuilder.aggregation(aggregationBuilder);
        SearchRequest searchRequest = new SearchRequest("documentes");
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(1000);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        Aggregations aggregations = searchResponse.getAggregations();
        Terms groupByTitleKeywordUploadUser = aggregations.get("group_by_titleKeyword_uploadUser");
        float totalScore = 0;
        System.out.println(searchResponse.getHits().getTotalHits());
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            float score = hit.getScore(); // 获取搜索结果的得分
            totalScore += score; // 将得分添加到总分数中
        }
        float avgScore = totalScore / searchResponse.getHits().getTotalHits().value; // 计算平均得分
        for (Terms.Bucket titleBucket : groupByTitleKeywordUploadUser.getBuckets()) {
            String documentTitle = titleBucket.getKeyAsString();
            Terms groupByUploadUser = titleBucket.getAggregations().get("group_by_uploadUser");
            for (Terms.Bucket userBucket : groupByUploadUser.getBuckets()) {
                DocumentFirstSearch documentFirstSearch = new DocumentFirstSearch();
                String user = userBucket.getKeyAsString();
                Long docCount = userBucket.getDocCount(); // 获取文档数量
                float score = avgScore * docCount; // 计算得分
                documentFirstSearch.setTitle(documentTitle);
                documentFirstSearch.setScore(score);
                documentFirstSearch.setUploadUser(user);
                documentFirstSearch.setTargetTimes(docCount);
                documentFirstSearchList.add(documentFirstSearch);
                //System.out.println("Title: " + documentTitle + ", User: " + user + ", Score: " + score + ", TargetTimes:" + docCount);
            }
        }

        return documentFirstSearchList;
    }


    @Override
    public List<DocumentSecondSearch> secondSearch(QueryBuilder queryBuilder) throws IOException {
        List<DocumentSecondSearch> documentSecondSearchList = new ArrayList<>();
        // 创建一个 SearchSourceBuilder 对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(1000);


        searchSourceBuilder.query(queryBuilder);
        // 设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("text");
        highlightBuilder.fragmentSize(100);
        highlightBuilder.requireFieldMatch(true);
        highlightBuilder.preTags("<span style=\"color:#CD0A08; font-weight:bold; background: #F6F8FA\">");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder).size(1000);
        highlightBuilder.requireFieldMatch(true);
        // 输出每条查询结果的得分和命中次数
        searchSourceBuilder.explain(true);
        // 执行查询
        SearchRequest searchRequest = new SearchRequest("documentes");
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // 处理查询结果
        SearchHits searchHits = searchResponse.getHits();
        for (SearchHit hit : searchHits) {
            // 处理每条查询结果的高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            Map<String, Object> map = hit.getSourceAsMap();
            //获取结果
            String titleRes = (String) map.get("title");

            int pageRes = (int) map.get("page");
            int source = (int) map.get("source");
            String imgName = (String) map.get("imgName");
            int paragraph = (int) map.get("paragraph");
            String textRes = highlightFields.get("text") != null ? highlightFields.get("text").getFragments()[0].toString() : hit.getSourceAsMap().get("text").toString();
            // 输出得分和命中次数
            float score = hit.getScore();
            Explanation explanation = hit.getExplanation();
            int hitCount = explanation.getDetails()[0].getDetails().length;
            DocumentSecondSearch documentSecondSearch = new DocumentSecondSearch(pageRes, source, textRes, imgName, paragraph, score, hitCount);
            documentSecondSearchList.add(documentSecondSearch);
            //System.out.println("title:" + titleRes + "   " + "score:" + score + "  " + "Count:" + hitCount + "  " + "textRes" + textRes);
            //System.out.println(map);

        }
        return documentSecondSearchList;
    }

    @Override
    public boolean deleteByTitleAndAndUploadUser(String title, String uploadUser) {
        return esRepository.deleteByTitleAndUploadUser(title, uploadUser);
    }


}

