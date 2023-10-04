package edu.njucm.retrieve.dao;

import edu.njucm.retrieve.model.Document;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {
    Document findByTitleAndUploadUser(String title, String uploadUser);

    List<Document> findAllByUploadUser(String uploadUser);

    Document findByDocumentId(Long documentId);

    @Query("select new Map(uploadTime,count(uploadTime) ) from Document group by uploadTime order by uploadTime desc")
    List<Map<String, Object>> findRecentUploads();

    @Query("select new Map(uploadTime,count(uploadTime) ) from Document group by uploadTime order by uploadTime")
    List<Map<String, Object>> findTotalUploads();

    List<Document> findByTitleLike(String content);

    List<Document> findByAuthorsLike(String content);

    List<Document> findByTagsLike(String content);

    List<Document> findAll();
}
