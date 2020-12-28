package com.evan.wj.pojo;

import lombok.Data;

@Data
public class Category {

    private Integer id;

    private String name;

    public Category() {
        super();
    }

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}