package edu.njucm.retrieve.services.Impl;

import edu.njucm.retrieve.dao.BrowsingHistoryRepository;
import edu.njucm.retrieve.model.BrowsingHistory;
import edu.njucm.retrieve.services.BrowsingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.util.List;

@Service
public class BrowsingHistoryServiceImpl implements BrowsingHistoryService {

    @Autowired
    BrowsingHistoryRepository browsingHistoryRepository;
    @Override
    public int addHistory(BrowsingHistory browsingHistory) {
        if(browsingHistoryRepository.existsByUploadUserAndUsernameAndTitle(browsingHistory.getUploadUser(),browsingHistory.getUsername(),browsingHistory.getTitle())){
            List<BrowsingHistory> browsingHistories = browsingHistoryRepository.findByUploadUserAndUsernameAndTitle(browsingHistory.getUploadUser(),browsingHistory.getUsername(),browsingHistory.getTitle());
            browsingHistory.setId(browsingHistories.get(0).getId());
            browsingHistoryRepository.save(browsingHistory);
        }
        browsingHistoryRepository.save(browsingHistory);
        return 0;
    }

    @Override
    public List<BrowsingHistory> loadAll(String username, Date date) {
        return browsingHistoryRepository.findAllByUsernameAndDate(username,date);
    }
}
