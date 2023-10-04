package edu.njucm.retrieve.services;

public interface ElasticsearchIndexService {
    boolean createIndexAndMapping(Class<?> classType);

}
