package edu.njucm.retrieve;

import edu.njucm.retrieve.dao.DocumentRepository;
import edu.njucm.retrieve.model.Document;
import edu.njucm.retrieve.services.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DocumentTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentService documentService;

    @Test
    void test() {
//        List<Document> documentlist = documentRepository.findAllByUploadUser("1234567");
//        Document[] documents = documentlist.toArray(new Document[documentlist.size()]);
//        System.out.println("---------------------------------------------");
//        //System.out.println(documentService.download(documents)[0]);
//        System.out.println(documentService.delete(documents));
        System.out.println(documentRepository.findByTitleAndUploadUser("基于卷积神经网络的时频域CT重建算法", "Test"));
    }

}
