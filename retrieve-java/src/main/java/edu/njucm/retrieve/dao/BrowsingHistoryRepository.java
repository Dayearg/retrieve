package edu.njucm.retrieve.dao;

import edu.njucm.retrieve.model.BrowsingHistory;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface BrowsingHistoryRepository extends CrudRepository<BrowsingHistory, Long> {
    List<BrowsingHistory> findAllByUsername(String username);

    Boolean existsByUploadUserAndUsernameAndTitle(String uploadUser,String userName,String title);

    List<BrowsingHistory> findByUploadUserAndUsernameAndTitle(String uploadUser,String userName,String title);

    List<BrowsingHistory> findAllByUsernameAndDate(String username, Date date);



}
