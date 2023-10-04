package edu.njucm.retrieve.model;

import java.util.Date;

public class DocumentFirstSearch {

    private String title;
    private int download;//下载次数
    private int click;//点击次数
    private int collect;//收藏次数
    private int totalPages; //文档总页数
    private float score;  //得分
    private Long targetTimes;  //命中次数
    private String uploadUser;  //上传用户
    private Date uploadTime;  //上传时间
    private String authors;  //作者
    private String tags;  //标签
    private Date published; //发表时间

    public DocumentFirstSearch() {
    }


    @Override
    public String toString() {
        return "DocumentFirstSearch{" +
                "title='" + title + '\'' +
                ", download=" + download +
                ", click=" + click +
                ", collect=" + collect +
                ", totalPages=" + totalPages +
                ", score=" + score +
                ", targetTimes=" + targetTimes +
                ", uploadUser='" + uploadUser + '\'' +
                ", uploadTime=" + uploadTime +
                ", authors='" + authors + '\'' +
                ", tags='" + tags + '\'' +
                ", published=" + published +
                '}';
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Long getTargetTimes() {
        return targetTimes;
    }

    public void setTargetTimes(Long targetTimes) {
        this.targetTimes = targetTimes;
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }
}
