package edu.njucm.retrieve.dao;

import edu.njucm.retrieve.model.SearchHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends CrudRepository<SearchHistory, Long> {
    List<SearchHistory> findAllByUsernameAndChoice(String username, String choice);

    Boolean existsSearchHistoriesByUsernameAndContentAndChoice(String username, String content, String choice);
}
