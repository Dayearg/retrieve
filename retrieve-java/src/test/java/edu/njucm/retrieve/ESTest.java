package edu.njucm.retrieve;

import edu.njucm.retrieve.model.DocumentSecondSearch;
import edu.njucm.retrieve.services.ElasticsearchService;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;


@SpringBootTest
public class ESTest {
    @Autowired
    private ElasticsearchService elasticsearchService;


    @Test
    void test() throws IOException {
        String inputkey = "医学";
        //List<DocumentFirstSearch> documents = elasticsearchService.findByTitleOrText(inputkey, inputkey);


    }

    @Test
    void test2() throws IOException {
        String title = "FOCUS 超声刀开放性甲状腺切除术的临床疗效评价";
        String textkey = "手术";

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("titleKeyword", title))
                .must(QueryBuilders.matchQuery("text", textkey).operator(Operator.AND));
        List<DocumentSecondSearch> seconddocuments = elasticsearchService.secondSearch(queryBuilder);


    }
}
