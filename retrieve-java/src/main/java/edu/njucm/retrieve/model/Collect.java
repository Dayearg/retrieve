package edu.njucm.retrieve.model;

import javax.persistence.*;

@Entity
@Table(name = "Collect")
public class Collect {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;  //用户名

    @Column(name = "fileId")
    private Long fileId;  //收藏文件的id

    public Collect() {
    }

    public Collect(String username, Long fileId) {
        this.username = username;
        this.fileId = fileId;
    }

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

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return "Collect{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fileId=" + fileId +
                '}';
    }
}
