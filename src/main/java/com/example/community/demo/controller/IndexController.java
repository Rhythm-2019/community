package com.example.community.demo.controller;

import com.example.community.demo.dto.GitHubUser;
import com.example.community.demo.mapper.UserMapper;
import com.example.community.demo.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired(required = false)
    private UserMapper userMapper;
    @GetMapping("/")
    public String index(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
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
        return "index";
    }
}


