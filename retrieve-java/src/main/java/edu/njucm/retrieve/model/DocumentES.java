package edu.njucm.retrieve.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "documentes", shards = 3, replicas = 1)
public class DocumentES {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String documentId;//主键，id
    @Field(type = FieldType.Text, analyzer = "ik_max_word")//细粒度分词
    private String title;//文章标题
    @Field(type = FieldType.Keyword, index = true)
    private String titleKeyword;
    @Field(type = FieldType.Text, analyzer = "ik_smart")//粗粒度分词
    private String authors;//作者
    @Field(type = FieldType.Text, analyzer = "ik_smart")//细粒度分词
    private String tags;//标签
    @Field(type = FieldType.Keyword, index = true)//不分词
    private String uploadUser;//上传用户
    @Field(type = FieldType.Keyword, index = true)//不分词
    private int page;//页数
    @Field(type = FieldType.Keyword, index = true)//不分词
    private int paragraph;//段落
    @Field(type = FieldType.Keyword, index = true)//不分词
    private int source;//来源0表示文本，1表示图片
    @Field(type = FieldType.Text, analyzer = "ik_max_word")//细粒度分词
    private String text;//文本
    @Field(type = FieldType.Text, analyzer = "ik_smart")//细粒度分词
    private String imgName;//图片名称


    public DocumentES() {
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        documentId = documentId;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getParagraph() {
        return paragraph;
    }

    public void setParagraph(int paragraph) {
        this.paragraph = paragraph;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitleKeyword() {
        return titleKeyword;
    }

    public void setTitleKeyword(String titleKeyword) {
        this.titleKeyword = titleKeyword;
    }

    @Override
    public String toString() {
        return "DocumentES{" +
                "DocumentId=" + documentId +
                ", title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", tags='" + tags + '\'' +
                ", uploadUser='" + uploadUser + '\'' +
                ", page=" + page +
                ", paragraph=" + paragraph +
                ", source=" + source +
                ", text='" + text + '\'' +
                ", imgName='" + imgName + '\'' +
                '}';
    }

    public DocumentES(edu.njucm.retrieve.model.Document document, int page, int paragraph, int source, String text, String imgName) {
        this.titleKeyword = document.getTitle();
        this.title = document.getTitle();
        this.authors = document.getAuthors();
        this.tags = document.getTags();
        this.uploadUser = document.getUploadUser();
        this.page = page;
        this.paragraph = paragraph;
        this.source = source;
        this.text = text;
        this.imgName = imgName;
    }
}