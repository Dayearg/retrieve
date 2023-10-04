package edu.njucm.retrieve.controller;

import edu.njucm.retrieve.model.BrowsingHistory;
import edu.njucm.retrieve.model.SearchHistory;
import edu.njucm.retrieve.services.BrowsingHistoryService;
import edu.njucm.retrieve.services.SearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/history")
@CrossOrigin
public class HistoryController {
    @Autowired
    private SearchHistoryService searchHistoryService;

    @Autowired
    private BrowsingHistoryService browsingHistoryService;

    @PostMapping("/search/get")
    public @ResponseBody List<SearchHistory> getSearchHistory(@RequestBody SearchHistory history) {
        return searchHistoryService.getSearchHistory(history);
    }

    @PostMapping("/browsing/get")
    public @ResponseBody List<BrowsingHistory> getSearchHistory(@RequestBody Map<String, Object> object) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date =  new Date(sdf.parse((String) object.get("date")).getTime());
        return browsingHistoryService.loadAll((String) object.get("username"),date);
    }

}
