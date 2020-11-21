package com.evan.wj.pojo;

import java.util.Date;

public class Book {
    private Integer id;

    private String cover;

    private String title;

    private String author;

    private Date date;

    private String press;

    private String abs;

    private Integer cid;

    private String category;

    public Book(Integer id, String cover, String title, String author, Date date, String press, String abs, Integer cid) {
        this.id = id;
        this.cover = cover;
        this.title = title;
        this.author = author;
        this.date = date;
        this.press = press;
        this.abs = abs;
        this.cid = cid;
    }

    public Book() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover == null ? null : cover.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press == null ? null : press.trim();
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs == null ? null : abs.trim();
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}