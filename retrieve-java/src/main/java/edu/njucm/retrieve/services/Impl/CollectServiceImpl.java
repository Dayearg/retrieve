package edu.njucm.retrieve.services.Impl;

import edu.njucm.retrieve.dao.CollectRepository;
import edu.njucm.retrieve.dao.DocumentRepository;
import edu.njucm.retrieve.model.Collect;
import edu.njucm.retrieve.model.Document;
import edu.njucm.retrieve.services.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    CollectRepository collectRepository;

    @Override
    public int collect(String username, String uploadUser, String title) {
        Document document = documentRepository.findByTitleAndUploadUser(title, uploadUser);
        if (document == null) {
            return 0;
        }
        Long fileId = document.getDocumentId();
        Collect collect = new Collect(username, fileId);
        collectRepository.save(collect);
        return 1;
    }

    @Override
    public List<Document> load(String username) {
        List<Collect> collects = collectRepository.findAllByUsername(username);
        List<Document> documents = new ArrayList<>();
        for (Collect collect : collects) {
            Document document = documentRepository.findByDocumentId(collect.getFileId());
            documents.add(document);
        }
        return documents;
    }
}
