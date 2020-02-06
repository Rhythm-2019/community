package com.example.community.demo.controller;

import com.example.community.demo.dto.GitHubUserDTO;
import com.example.community.demo.dto.PaginationDTO;
import com.example.community.demo.service.NotificationService;
import com.example.community.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired(required = false)
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "5") Integer size,
                          HttpServletRequest request,
                          Model model){

        //验证一下登录
        GitHubUserDTO gitHubUserDTO = (GitHubUserDTO) request.getSession().getAttribute("userInfo");

        if(gitHubUserDTO == null){
            return "index";
        }
        Integer unRead = notificationService.countUnread(gitHubUserDTO.getId());
        //判断一下是要跳转到那个页面
        if("question".equals(action)){
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的提问");
            //查询一下列表
            //加载列表,这里还要继续封装，吧页面的页数也放进去
            PaginationDTO paginationDTO = questionService.list(gitHubUserDTO.getId(), page, size);
            //返回前端
            model.addAttribute("pagination",paginationDTO);
            model.addAttribute("unRead",unRead);
        }
        if("reply".equals(action)){
            model.addAttribute("section","reply");
            model.addAttribute("sectionName","最新回复");
            PaginationDTO paginationDTO = notificationService.list(gitHubUserDTO.getId(), page, size);

            model.addAttribute("pagination",paginationDTO);
            //数字
            model.addAttribute("unRead",unRead);
        }


        return "profile";
    }
}
