package com.evan.wj.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class User {
//    @JsonIgnore
    private Integer id;

//    @JsonProperty("name")
    private String username;

    private String password;

    public User() {
        super();
    }

    public User(Integer id,String username,String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}