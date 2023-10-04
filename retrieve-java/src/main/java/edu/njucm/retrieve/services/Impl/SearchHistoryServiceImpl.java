package edu.njucm.retrieve.services.Impl;

import edu.njucm.retrieve.dao.SearchHistoryRepository;
import edu.njucm.retrieve.model.BrowsingHistory;
import edu.njucm.retrieve.model.SearchHistory;
import edu.njucm.retrieve.services.SearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchHistoryServiceImpl implements SearchHistoryService {

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Override
    public List<SearchHistory> getSearchHistory(SearchHistory history) {
        return searchHistoryRepository.findAllByUsernameAndChoice(history.getUsername(), history.getChoice());
    }

    @Override
    public Boolean addHistory(SearchHistory searchHistory) {
        if (searchHistoryRepository.existsSearchHistoriesByUsernameAndContentAndChoice(searchHistory.getUsername(), searchHistory.getContent(), searchHistory.getChoice())) {
            return false;
        } else {
            searchHistoryRepository.save(searchHistory);
            return true;
        }
    }
}
