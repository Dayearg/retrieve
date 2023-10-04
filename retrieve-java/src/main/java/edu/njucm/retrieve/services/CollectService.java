package edu.njucm.retrieve.services;

import edu.njucm.retrieve.model.Document;

import java.util.List;

public interface CollectService {
    int collect(String username, String uploadUser, String title);

    List<Document> load(String username);
}
