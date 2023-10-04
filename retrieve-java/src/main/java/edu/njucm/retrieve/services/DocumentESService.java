package edu.njucm.retrieve.services;

import edu.njucm.retrieve.model.Document;

public interface DocumentESService {
    Document read(String title, String uploadUser);
}
