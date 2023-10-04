package edu.njucm.retrieve.services.Impl;


import edu.njucm.retrieve.services.ElasticsearchIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchIndexServiceImp implements ElasticsearchIndexService {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    @Override
    public boolean createIndexAndMapping(Class<?> classType) {
        boolean index = elasticsearchRestTemplate.indexOps(classType).create();
        Document mapping = elasticsearchRestTemplate.indexOps(classType).createMapping();
        boolean mappingResult = elasticsearchRestTemplate.indexOps(classType).putMapping(mapping);
        return index && mappingResult;
    }


}
