package edu.njucm.retrieve.model;

import javax.persistence.*;


@Entity
@Table(name = "History")
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "content")
    private String content;

    @Column(name = "choice")
    private String choice;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public SearchHistory() {
    }

    public SearchHistory(Long id, String username, String content, String choice) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.choice = choice;
    }
}
