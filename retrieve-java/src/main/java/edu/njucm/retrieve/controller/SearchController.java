package edu.njucm.retrieve.controller;

import edu.njucm.retrieve.dao.CollectRepository;
import edu.njucm.retrieve.dao.DocumentRepository;
import edu.njucm.retrieve.method.SearchSort;
import edu.njucm.retrieve.model.*;
import edu.njucm.retrieve.services.BrowsingHistoryService;
import edu.njucm.retrieve.services.ElasticsearchService;
import edu.njucm.retrieve.services.SearchHistoryService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Controller
@RequestMapping("/search")
@CrossOrigin
public class SearchController {

    @Autowired
    ElasticsearchService elasticsearchService;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    SearchHistoryService searchHistoryService;

    @Autowired
    BrowsingHistoryService browsingHistoryService;

    @Autowired
    CollectRepository collectRepository;

    @Value("${file.ip}")
    String ip;

    @PostMapping("/first")
    public @ResponseBody List<DocumentFirstSearch> firstSearch(@RequestBody SearchContent[] searchContents) {
        SearchHistory searchHistory = new SearchHistory();
        for (SearchContent searchContent : searchContents) {
            searchHistory.setUsername(searchContent.getUsername());
            searchHistory.setChoice(searchContent.getType());
            searchHistory.setContent(searchContent.getContent());
            searchHistoryService.addHistory(searchHistory);
        }
        BoolQueryBuilder boolQueryBuilder;
        List<DocumentFirstSearch> documentFirstSearchList;
        String text = searchContents[0].getContent();
        if (searchContents[0].getType().equals("default")) {
            if (searchContents[0].getPrecise()) {
                boolQueryBuilder = QueryBuilders.boolQuery()
//                        .must(QueryBuilders.queryStringQuery(text).field("title").defaultOperator(Operator.OR))
                        .should(QueryBuilders.matchQuery("title", text).operator(Operator.OR))
                        .must(QueryBuilders.queryStringQuery("\"" +text+"\"").field("text").defaultOperator(Operator.OR));
            } else {
                boolQueryBuilder = QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchQuery("title", text).operator(Operator.OR))
                        .should(QueryBuilders.matchQuery("text", text).operator(Operator.OR));
            }
        } else {
            if (searchContents[0].getPrecise()) {
                boolQueryBuilder = QueryBuilders.boolQuery()
                        .must(QueryBuilders.queryStringQuery("\"" +text+"\"").field(searchContents[0].getType()).defaultOperator(Operator.OR));
            } else {
                boolQueryBuilder = QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchQuery(searchContents[0].getType(), text).operator(Operator.OR));
            }

        }
        if (searchContents.length > 1) {
            for (int i = 1; i < searchContents.length; i++) {
                text = searchContents[i].getContent();
                switch (searchContents[i].getOperation()) {
                    case "AND":
                        if (searchContents[i].getPrecise()) {
                            boolQueryBuilder.must(QueryBuilders.matchQuery(searchContents[i].getType(), text).operator(Operator.AND));
                        } else {
                            boolQueryBuilder.should(QueryBuilders.matchQuery(searchContents[i].getType(), text).operator(Operator.AND));
                        }
                        break;
                    case "OR":
                        if (searchContents[i].getPrecise()) {
                            boolQueryBuilder.must(QueryBuilders.matchQuery(searchContents[i].getType(), text).operator(Operator.OR));
                        } else {
                            boolQueryBuilder.should(QueryBuilders.matchQuery(searchContents[i].getType(), text).operator(Operator.OR));
                        }
                        break;
                    case "NOT":
                        boolQueryBuilder.mustNot(QueryBuilders.matchQuery(searchContents[i].getType(), text).operator(Operator.AND));
                        break;
                    default:
                        break;
                }
            }
        }

        try {
            documentFirstSearchList = elasticsearchService.firstSearch(boolQueryBuilder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<DocumentFirstSearch> documentFirstSearchListResult = new ArrayList<>();
        if (documentFirstSearchList.size() > 0) {
            for (int i = 0; i < documentFirstSearchList.size(); i++) {
                DocumentFirstSearch documentFirstSearch = documentFirstSearchList.get(i);
                Document document = documentRepository.findByTitleAndUploadUser(documentFirstSearch.getTitle(), documentFirstSearch.getUploadUser());
                if (document.getStatus() == 1 && !searchContents[0].getUsername().equals(document.getUploadUser())) {
                    continue;
                }
                if (!searchContents[0].getAll() && !document.getUploadUser().equals(searchContents[0].getUsername())) {
                    continue;
                }
                documentFirstSearch.setDownload(document.getDownload());
                documentFirstSearch.setClick(document.getClick());
                documentFirstSearch.setAuthors(document.getAuthors());
                documentFirstSearch.setTags(document.getTags());
                documentFirstSearch.setPublished(document.getPublished());
                documentFirstSearch.setTotalPages(document.getTotalPages());
                documentFirstSearch.setUploadTime(document.getUploadTime());
                Integer collectNum = collectRepository.collectNum(document.getDocumentId());
                documentFirstSearch.setCollect(collectNum);
                documentFirstSearchListResult.add(documentFirstSearch);
            }
        }
        switch (searchContents[0].getSortIndex()) {
            case 1:
                documentFirstSearchListResult.sort(Comparator.comparing(DocumentFirstSearch::getDownload).reversed());
                break;
            case 2:
                documentFirstSearchListResult.sort(Comparator.comparing(DocumentFirstSearch::getCollect).reversed());
                break;
            case 3:
                documentFirstSearchListResult.sort(Comparator.comparing(DocumentFirstSearch::getClick).reversed());
                break;
            default:
                break;
        }

        return documentFirstSearchListResult;
    }

    @PostMapping("/second")
    public @ResponseBody DocumentSecondSearch[] secondSearch(@RequestBody SecondSearchContent secondSearchContent) {
        String title = secondSearchContent.getTitle();
        String uploadUser = secondSearchContent.getUploadUser();
        Document document = documentRepository.findByTitleAndUploadUser(secondSearchContent.getTitle(), secondSearchContent.getUploadUser());
        document.setClick(document.getClick() + 1);
        documentRepository.save(document);
        BrowsingHistory browsingHistory = new BrowsingHistory(secondSearchContent.getUsername(), new Date(new java.util.Date().getTime()), secondSearchContent.getUploadUser(), secondSearchContent.getTitle());
        browsingHistoryService.addHistory(browsingHistory);
        String fileName = document.getFileName();
        SearchContent[] searchContents = secondSearchContent.getSearchContents();
        List<DocumentSecondSearch> documentSecondSearchList;
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        String text = searchContents[0].getContent();
        if (searchContents[0].getType().equals("default") || searchContents[0].getType().equals("text")) {
            boolQuery.must(QueryBuilders.termQuery("titleKeyword", title))
                    .must(QueryBuilders.matchQuery("text", text).operator(Operator.OR));
        } else {
            boolQuery
                    .must(QueryBuilders.termQuery("titleKeyword", title));
        }
        if (searchContents.length > 1) {
            for (int i = 1; i < searchContents.length; i++) {
                if (searchContents[i].getType().equals("source")) {
                    boolQuery.must(QueryBuilders.matchQuery(searchContents[i].getType(), searchContents[i].getContent()));
                    continue;
                }
                if (!searchContents[i].getType().equals("text") && !searchContents[i].getType().equals("default"))
                    continue;
                text = searchContents[i].getContent();
                switch (searchContents[i].getOperation()) {
                    case "AND":
                        boolQuery.must(QueryBuilders.matchQuery(searchContents[i].getType(), text).operator(Operator.AND));
                        break;
                    case "OR":
                        boolQuery.must(QueryBuilders.matchQuery(searchContents[i].getType(), text).operator(Operator.OR));
                        break;
                    default:
                        break;
                }
            }
        }
        try {
            documentSecondSearchList = elasticsearchService.secondSearch(boolQuery);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (documentSecondSearchList.size() < 1) {
            return null;
        }
        DocumentSecondSearch[] documentSecondSearches = new DocumentSecondSearch[documentSecondSearchList.size()];
        for (int i = 0; i < documentSecondSearches.length; i++) {
            documentSecondSearches[i] = documentSecondSearchList.get(i);
            documentSecondSearches[i].setImgUrl(ip + uploadUser + "/" + fileName + "/Picture/" + documentSecondSearches[i].getImgUrl() + ".png");
        }
        return SearchSort.sort(documentSecondSearches);
    }


}
