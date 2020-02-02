package com.example.community.demo.mapper;

import com.example.community.demo.model.Question;

public interface QuestionExtMapper {

    int incView(Question record);

    int incComment(Question record);

  }