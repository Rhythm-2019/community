package com.example.community.demo.dto;

import lombok.Data;

@Data
public class GitHubUserDTO {
    private String login;
    private Integer id;
    private String bio;
    private String avatarUrl;
    private Integer accountId;
}
