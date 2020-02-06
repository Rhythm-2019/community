package com.example.community.demo.dto;

import lombok.Data;

@Data
public class QuestionQueryDTO {
    private String keyword;
    private Integer offset;
    private Integer size;
}
