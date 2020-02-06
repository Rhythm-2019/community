package com.example.community.demo.controller;

import com.example.community.demo.dto.GitHubUserDTO;
import com.example.community.demo.dto.NotificationDTO;
import com.example.community.demo.enums.NotificationTypeEnum;
import com.example.community.demo.model.Notification;
import com.example.community.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String notification(@PathVariable(name = "id") Integer id,
                               HttpServletRequest request){

        //判断一下登录
        GitHubUserDTO userInfo = (GitHubUserDTO)request.getSession().getAttribute("userInfo");

        if(userInfo == null){
            return "index";
        }
        NotificationDTO notificationDTO = notificationService.read(id, userInfo.getId());
        //保险起见他是评论还是问题，其实没什么必要
        if(notificationDTO.getType() == NotificationTypeEnum.REPLY.getType() ||
                notificationDTO.getType() == NotificationTypeEnum.COMMENT.getType() ){
            return "redirect:/question/" + notificationDTO.getOuterId();
        }
        return "index";
    }
}
