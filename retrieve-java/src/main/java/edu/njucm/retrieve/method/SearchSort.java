package edu.njucm.retrieve.method;

import edu.njucm.retrieve.model.DocumentSecondSearch;
import edu.njucm.retrieve.model.SecondSearchSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchSort {

    public static DocumentSecondSearch[] sort(DocumentSecondSearch[] documentSecondSearches) {
        List<SecondSearchSort> secondSearchSortList = new ArrayList<>();
        boolean sign = false;
        // 按照页数分组
        for (DocumentSecondSearch documentSecondSearch : documentSecondSearches) {
            if (sign == true) {
                for (SecondSearchSort list : secondSearchSortList) {
                    if (list.getPage() == documentSecondSearch.getPage()) {
                        list.setScore(list.getScore() + documentSecondSearch.getScore());
                        list.setDocumentSecondSearchList(documentSecondSearch);
                        sign = true;
                        break;
                    }
                    sign = false;
                }
            }
            if (sign == false) {
                secondSearchSortList.add(new SecondSearchSort(documentSecondSearch.getPage(), documentSecondSearch.getScore(), documentSecondSearch));
                sign = true;
            }
        }
        // 跟据每页的分数排序
        Collections.sort(secondSearchSortList);

        int i = 0;
        DocumentSecondSearch[] documentSecondSearchesSort = new DocumentSecondSearch[documentSecondSearches.length];
        while (i < documentSecondSearchesSort.length) {
            for (SecondSearchSort list : secondSearchSortList) {
                for (DocumentSecondSearch documentSecondSearch : list.getDocumentSecondSearchList()) {
                    documentSecondSearchesSort[i] = documentSecondSearch;
                    i++;
                }
            }
        }
        return documentSecondSearchesSort;
    }

}
