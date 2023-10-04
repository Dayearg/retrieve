package edu.njucm.retrieve.services;

import edu.njucm.retrieve.model.Document;
import edu.njucm.retrieve.model.DocumentFirstSearch;
import edu.njucm.retrieve.model.SearchContent;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface DocumentService {
    Document add(MultipartFile file, String username);

    String[] download(Document[] documents);

    boolean delete(Document[] documents);

    List<Document> refresh(String username);

    Document revise(Document document);

    String preview(Document document);

    Map<String, Long> recentUploads();

    Map<String, Long> totalUploads();

    List<Document> search(SearchContent searchContent);

}
