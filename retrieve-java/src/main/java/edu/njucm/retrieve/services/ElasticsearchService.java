package edu.njucm.retrieve.services;

import edu.njucm.retrieve.model.DocumentES;
import edu.njucm.retrieve.model.DocumentFirstSearch;
import edu.njucm.retrieve.model.DocumentSecondSearch;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.IOException;
import java.util.List;


public interface ElasticsearchService {
    boolean save(DocumentES documentES);

    boolean saveAll(List<DocumentES> documents);

    List<DocumentFirstSearch> firstSearch(BoolQueryBuilder boolQueryBuilder) throws IOException;

    List<DocumentSecondSearch> secondSearch(QueryBuilder queryBuilder) throws IOException;

    boolean deleteByTitleAndAndUploadUser(String title, String uploadUser);
}
