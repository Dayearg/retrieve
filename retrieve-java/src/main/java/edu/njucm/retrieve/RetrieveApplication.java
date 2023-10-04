package edu.njucm.retrieve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "edu.njucm.retrieve")
public class RetrieveApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetrieveApplication.class, args);
    }

}
