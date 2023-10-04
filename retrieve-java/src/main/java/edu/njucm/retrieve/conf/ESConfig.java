package edu.njucm.retrieve.conf;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ESConfig {
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        //ES的 ip 端口 协议(固定)
        RestClientBuilder http = RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"));
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(http);
        return restHighLevelClient;
    }
}

