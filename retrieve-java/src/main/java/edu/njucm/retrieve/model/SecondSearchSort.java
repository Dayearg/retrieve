package edu.njucm.retrieve.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecondSearchSort implements Comparable<SecondSearchSort> {
    int page;
    double score;
    List<DocumentSecondSearch> documentSecondSearchList = new ArrayList<>();

    public SecondSearchSort() {
    }

    public SecondSearchSort(int page, double score, DocumentSecondSearch documentSecondSearch) {
        this.page = page;
        this.score = score;
        this.documentSecondSearchList.add(documentSecondSearch);
    }

    @Override
    public int compareTo(SecondSearchSort secondSearchSort) {
        Collections.sort(this.documentSecondSearchList);
        //return (int) (secondSearchSort.score - this.score);
        if (this.score < secondSearchSort.score)
            return 1;
        else if (this.score > secondSearchSort.score)
            return -1;
        else {
            return 0;
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public List<DocumentSecondSearch> getDocumentSecondSearchList() {
        return documentSecondSearchList;
    }

    public void setDocumentSecondSearchList(DocumentSecondSearch documentSecondSearch) {
        this.documentSecondSearchList.add(documentSecondSearch);
    }
}
