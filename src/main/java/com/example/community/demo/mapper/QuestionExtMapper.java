package com.example.community.demo.mapper;

import com.example.community.demo.dto.QuestionQueryDTO;
import com.example.community.demo.model.Question;

import java.util.List;

public interface QuestionExtMapper {

    int incView(Question record);

    int incComment(Question record);

    List<Question> selectTag(Question record);

    Integer count();

    Integer countById(Integer userId);

    Integer countBySearch(String keyword);

    List<Question> select(QuestionQueryDTO questionQueryDTO);
}