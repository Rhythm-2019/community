package com.example.community.demo.controller;

import com.example.community.demo.dto.AccesstokenDTO;
import com.example.community.demo.dto.GitHubUser;
import com.example.community.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setClient_id(client_id);
        accesstokenDTO.setClient_secret(client_secret);
        accesstokenDTO.setCode(code);
        accesstokenDTO.setState(state);
        accesstokenDTO.setRedirect_uri(redirect_url);
        String token = githubProvider.getAccesstoken(accesstokenDTO);
        GitHubUser userInfo = githubProvider.getUserInfo(token);


        return "index";
    }
}
