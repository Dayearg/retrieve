package edu.njucm.retrieve;

import edu.njucm.retrieve.services.DocumentESService;
import edu.njucm.retrieve.services.DocumentService;
import edu.njucm.retrieve.services.ElasticsearchService;
import edu.njucm.retrieve.services.RPCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RPCTests {
    @Autowired
    private DocumentService documentService;

    @Autowired
    private RPCService rpcService;

    @Autowired
    private DocumentESService documentESService;

    @Autowired
    private ElasticsearchService elasticsearchService;


}
