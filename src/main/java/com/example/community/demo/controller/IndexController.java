package com.example.community.demo.controller;

import com.example.community.demo.dto.GitHubUser;
import com.example.community.demo.dto.PaginationDTO;
import com.example.community.demo.dto.QuestionDTO;
import com.example.community.demo.mapper.UserMapper;
import com.example.community.demo.model.QuestionModel;
import com.example.community.demo.model.UserModel;
import com.example.community.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
                        Model model){


        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    //需要对token进行校验
                    UserModel userModel = userMapper.findByToken(token);
                    if(userModel != null){
                        GitHubUser gitHubUser = new GitHubUser();
                        gitHubUser.setLogin(userModel.getName());
                        gitHubUser.setId(userModel.getId());
                        request.getSession().setAttribute("userInfo",gitHubUser);
                        break;
                    }

                }
            }
        }
        //加载列表,这里还要继续封装，吧页面的页数也放进去
        PaginationDTO paginationDTO = questionService.list(page, size);
        //返回前端
        model.addAttribute("pagination",paginationDTO);

        return "index";
    }
}


