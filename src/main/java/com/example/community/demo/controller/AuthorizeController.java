package com.example.community.demo.controller;

import com.example.community.demo.dto.AccesstokenDTO;
import com.example.community.demo.dto.GitHubUser;
import com.example.community.demo.mapper.UserMapper;
import com.example.community.demo.model.UserModel;
import com.example.community.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String client_id;

    @Value("${github.client.secret}")
    private String client_secret;

    @Value("${github.redirect.url}")
    private String redirect_url;

    @Autowired(required = false)
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                            HttpServletRequest request,
                            HttpServletResponse response){
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setClient_id(client_id);
        accesstokenDTO.setClient_secret(client_secret);
        accesstokenDTO.setCode(code);
        accesstokenDTO.setState(state);
        accesstokenDTO.setRedirect_uri(redirect_url);
        String githubToken = githubProvider.getAccesstoken(accesstokenDTO);
        GitHubUser userInfo = githubProvider.getUserInfo(githubToken);

        if(userInfo != null){
            //Login
            UserModel user = new UserModel();
            user.setAccountId(String.valueOf(userInfo.getId()));
            user.setName(userInfo.getLogin());
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());

            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));

        }else{
            return "redirect:/";
        }
        return "redirect:/";

    }
}
