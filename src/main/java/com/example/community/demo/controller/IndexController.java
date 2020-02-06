package com.example.community.demo.controller;

import com.example.community.demo.dto.PaginationDTO;
import com.example.community.demo.mapper.UserMapper;
import com.example.community.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired(required = false)
    private QuestionService questionService;

    @Autowired(required = false)
    private UserMapper userMapper;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5") Integer size,
                        @RequestParam(name = "keyword",required = false) String keyword,
                        Model model){

        //加载列表,这里还要继续封装，吧页面的页数也放进去
       PaginationDTO paginationDTO = questionService.list(page, size, keyword);

        //返回前端
        model.addAttribute("pagination",paginationDTO);
        if(keyword != null){
            model.addAttribute("keyword",keyword);
        }

        return "index";
    }
}


