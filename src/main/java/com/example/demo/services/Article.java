package com.example.demo.services;

import java.time.LocalDateTime;


public class Article {

    private static long nextId=1;

    private Long id;
    private LocalDateTime date;
    private String title;
    private String lead;
    private String content;
    private String author;

    public Article(String title, String lead, String content, String author) {
        this.id = nextId++;
        this.date = LocalDateTime.now();
        this.title = title;
        this.lead = lead;
        this.content = content;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
