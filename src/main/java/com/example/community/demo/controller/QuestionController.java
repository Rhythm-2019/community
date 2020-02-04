package com.example.community.demo.controller;

import com.example.community.demo.dto.CommentDTO;
import com.example.community.demo.dto.QuestionDTO;
import com.example.community.demo.enums.CommentTypeEnum;
import com.example.community.demo.model.Question;
import com.example.community.demo.service.CommentService;
import com.example.community.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model){

        QuestionDTO questionDTO = questionService.getById(id);
        //添加阅读数
        questionService.incView(id);
        //获得回复列表
        List<CommentDTO> commentDTOS = commentService.listByQuestionId(id, CommentTypeEnum.QUESTION);
        //获取相关问题的列表
        List<QuestionDTO> aboutList = questionService.selectRelated(questionDTO);
        model.addAttribute("question",questionDTO);
        model.addAttribute("commentDTOS",commentDTOS);
        model.addAttribute("aboutList",aboutList);
        return "question";
    }
}
