package com.example.community.demo.model;

import lombok.Data;

@Data
public class UserModel {
    private Integer id;
    private String accountId;
    private String name;
    private String token;
    private String bio;
    private long gmtCreate;
    private long gmtModified;
    private String avatarUrl;

}
