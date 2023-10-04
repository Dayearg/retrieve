package edu.njucm.retrieve.services;

import edu.njucm.retrieve.model.BrowsingHistory;
import edu.njucm.retrieve.model.Document;

import java.sql.Date;
import java.util.List;

public interface BrowsingHistoryService {
    int addHistory(BrowsingHistory browsingHistory);

    List<BrowsingHistory> loadAll(String username, Date date);
}
