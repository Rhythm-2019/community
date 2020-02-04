package com.example.community.demo.mapper;

import com.example.community.demo.model.Question;

import java.util.List;

public interface QuestionExtMapper {

    int incView(Question record);

    int incComment(Question record);

    List<Question> selectTag(Question record);
  }