package edu.njucm.retrieve.model;

public class DocumentSecondSearch implements Comparable<DocumentSecondSearch> {

    private int page;  //文档所在页
    private int source;  //内容来源
    private String text;  //文本
    private String imgUrl;  //图片URL
    private int paragraph;  //段落
    private double score;  //分数
    private int targetTimes;  //总命中次数

    public DocumentSecondSearch() {
    }

    public DocumentSecondSearch(int page, int source, String text, String imgUrl, int paragraph, double score, int targetTimes) {
        this.page = page;
        this.source = source;
        this.text = text;
        this.imgUrl = imgUrl;
        this.paragraph = paragraph;
        this.score = score;
        this.targetTimes = targetTimes;
    }

    @Override
    public String toString() {
        return "DocumentSecondSearch{" +
                "page=" + page +
                ", source=" + source +
                ", text='" + text + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", paragraph=" + paragraph +
                ", score=" + score +
                ", targetTimes=" + targetTimes +
                '}';
    }

    @Override
    public int compareTo(DocumentSecondSearch documentSecondSearch) {
        return this.paragraph - documentSecondSearch.paragraph;
        /*if (this.page > documentSecondSearch.page)
            return 1;
        else if (this.page < documentSecondSearch.page)
            return -1;
        else {
            if (this.paragraph > documentSecondSearch.paragraph)
                return 1;
            else if (this.paragraph < documentSecondSearch.paragraph)
                return -1;
            else
                return 0;
        }*/
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getParagraph() {
        return paragraph;
    }

    public void setParagraph(int paragraph) {
        this.paragraph = paragraph;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getTargetTimes() {
        return targetTimes;
    }

    public void setTargetTimes(int targetTimes) {
        this.targetTimes = targetTimes;
    }
}
