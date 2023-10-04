package edu.njucm.retrieve.services;

import edu.njucm.retrieve.model.SearchHistory;

import java.util.List;

public interface SearchHistoryService {
    List<SearchHistory> getSearchHistory(SearchHistory history);

    Boolean addHistory(SearchHistory searchHistory);
}
