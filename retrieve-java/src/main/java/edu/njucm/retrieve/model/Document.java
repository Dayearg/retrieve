package edu.njucm.retrieve.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long documentId;//主键，id

    @Column(name = "title")
    private String title;//文章标题

    @Column(name = "authors")
    private String authors;//作者

    @Column(name = "published")
    private Date published;//发表时间

    @Column(name = "tags")
    private String tags;//标签

    @Column(name = "uploadTime")
    private Date uploadTime;//上传时间

    @Column(name = "uploadUser")
    private String uploadUser;//上传用户

    @Column(name = "download")
    private int download;//下载次数

    @Column(name = "click")
    private int click = 0;//点击次数

    @Column(name = "status")
    private int status = -1;//状态

    @Column(name = "fileName")
    private String fileName;//文件位置

    @Column(name = "totalPages")
    private int totalPages; //文档总页数

    public Document() {
    }

    public Document(String title, String authors, Date published, String tags, Date uploadTime, String uploadUser, String fileName, int totalPages) {
        this.title = title;
        this.authors = authors;
        this.published = published;
        this.tags = tags;
        this.uploadTime = uploadTime;
        this.uploadUser = uploadUser;
        this.download = 0;
        this.click = 0;
        this.status = 0;
        this.fileName = fileName;
        this.totalPages = totalPages;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }
}
