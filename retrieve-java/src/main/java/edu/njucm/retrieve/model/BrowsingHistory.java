package edu.njucm.retrieve.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "BrowsingHistory")
public class BrowsingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;  //用户名

    @Column(name = "date")
    private Date date;
    @Column(name = "uploadUser")
    private String uploadUser;  //收藏文件的id

    @Column(name = "title")
    private String title;//标题

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BrowsingHistory(String username, Date date, String uploadUser, String title) {
        this.username = username;
        this.date = date;
        this.uploadUser = uploadUser;
        this.title = title;
    }

    public BrowsingHistory() {
    }


}
