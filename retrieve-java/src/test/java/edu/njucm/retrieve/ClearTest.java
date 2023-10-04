package edu.njucm.retrieve;

import edu.njucm.retrieve.dao.DocumentRepository;
import edu.njucm.retrieve.model.Document;
import edu.njucm.retrieve.services.DocumentESService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;


@SpringBootTest
public class ClearTest {

    @Value("${file.location}")
    String path;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentESService documentESService;

    @Test
    public void clear() {
        String userName = "Test";
        String location = path + userName;
        List<Document> documentList = documentRepository.findAllByUploadUser(userName);
        File dir = new File(location);
        String[] files = dir.list();
        for (int i = 0; i < files.length; i++) {
            int k = 1;
            for (int j = 0; j < documentList.size(); j++) {
                if (files[i].equals(documentList.get(j).getFileName())) {
                    k = 0;
                }
            }
            if (k == 1) {
                System.out.println(files[i]);
            }
        }

    }

    @Test
    public void reclear() {
        String userName = "Test";
        String location = path + userName;
        List<Document> documentList = documentRepository.findAllByUploadUser(userName);
        File dir = new File(location);
        String[] files = dir.list();
        for (int i = 0; i < documentList.size(); i++) {
            int k = 1;
            for (int j = 0; j < files.length; j++) {
                if (files[j].equals(documentList.get(i).getFileName())) {
                    k = 0;
                }
            }
            if (k == 1) {
                System.out.println(files[i]);
            }
        }

    }

    @Test
    public void reload() throws InterruptedException {
        String userName = "Test";
        String location = path + userName;
        File dir = new File(location);
        String[] files = dir.list();
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i]);
            documentESService.read(files[i], "Test");
        }
    }
}
