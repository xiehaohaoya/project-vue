package com.evan.wj.pojo;

import lombok.Data;

import java.util.Date;

@Data
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

    public Book() {
        super();
    }

    public Book(Integer id, String cover, String title, String author, Date date, String press, String abs, Integer cid, String category) {
        this.id = id;
        this.cover = cover;
        this.title = title;
        this.author = author;
        this.date = date;
        this.press = press;
        this.abs = abs;
        this.cid = cid;
        this.category = category;
    }
}