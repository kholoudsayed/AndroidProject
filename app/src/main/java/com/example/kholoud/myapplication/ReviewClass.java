package com.example.kholoud.myapplication;

public class ReviewClass {
// DEF********************************************************************
    private String author;
    private String content;
    private String id;
    private String url;
// Constractor***********************
    public ReviewClass(String author, String content, String id, String url) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
    }
// getter and setters methods **********************
    public String getAuthor() {
        return author;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
