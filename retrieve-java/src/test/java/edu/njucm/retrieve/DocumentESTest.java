package edu.njucm.retrieve;

import edu.njucm.retrieve.dao.ESRepository;
import edu.njucm.retrieve.services.DocumentESService;
import edu.njucm.retrieve.services.ElasticsearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DocumentESTest {

    @Autowired
    private DocumentESService documentServiceES;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private ESRepository repository;

    @Test
    void test() {
//        String title = "ENSITE_NAVX和双LAS_省略_左心房线性消融治疗阵发性心房颤动_陈明龙";
//        List<DocumentES> res = repository.findByTitle(title);
//        System.out.println("");

    }


}
