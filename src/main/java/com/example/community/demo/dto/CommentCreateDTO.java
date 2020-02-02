package com.example.community.demo.dto;

import lombok.Data;

@Data
public class CommentCreateDTO {
    private String comment;
    private Integer parentId;
    private Integer type;
}
