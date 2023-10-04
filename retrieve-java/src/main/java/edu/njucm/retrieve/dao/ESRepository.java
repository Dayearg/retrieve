package edu.njucm.retrieve.dao;

import edu.njucm.retrieve.model.DocumentES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ESRepository extends ElasticsearchRepository<DocumentES, String> {

    List<DocumentES> findByTextOrTitle(String text, String title);

    boolean deleteByTitleAndUploadUser(String title, String uploadUser);
}
