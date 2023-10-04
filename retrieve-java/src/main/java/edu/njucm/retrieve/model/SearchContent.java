package edu.njucm.retrieve.model;

public class SearchContent {
    String type;//关键词类型
    String content;//检索内容
    String operation;//操作
    Boolean precise; //精确搜索
    Integer sortIndex;//排序索引 1:下载 2:收藏 3:点击
    String username;//用户名
    Boolean all;//是否检索全部数据库

    public SearchContent() {
    }


    public Boolean getAll() {
        return all;
    }

    public void setAll(Boolean all) {
        this.all = all;
    }

    public SearchContent(String type, String content, String operation, Boolean precise, Integer sortIndex, String username, Boolean all) {
        this.type = type;
        this.content = content;
        this.operation = operation;
        this.precise = precise;
        this.sortIndex = sortIndex;
        this.username = username;
        this.all = all;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Boolean getPrecise() {
        return precise;
    }

    public void setPrecise(Boolean precise) {
        this.precise = precise;
    }

}
