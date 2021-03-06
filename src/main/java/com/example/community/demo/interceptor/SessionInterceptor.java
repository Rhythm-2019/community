package com.example.community.demo.interceptor;

import com.example.community.demo.dto.GitHubUserDTO;
import com.example.community.demo.enums.NotificationStatusEnum;
import com.example.community.demo.mapper.NotificationMapper;
import com.example.community.demo.mapper.UserMapper;
import com.example.community.demo.model.Notification;
import com.example.community.demo.model.User;
import com.example.community.demo.model.UserExample;
import com.example.community.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private NotificationService notificationService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //验证登录
        Cookie[] cookies = request.getCookies();
        GitHubUserDTO gitHubUserDTO = new GitHubUserDTO();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    //需要对token进行校验
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);

                    if(users.size() != 0){
                        User userModel = users.get(0);
                        gitHubUserDTO.setLogin(userModel.getName());
                        gitHubUserDTO.setId(userModel.getId());
                        gitHubUserDTO.setAvatarUrl(userModel.getAvatarUrl());
                        Integer unRead = notificationService.countUnread(userModel.getId());
                        request.getSession().setAttribute("unRead", unRead);
                        request.getSession().setAttribute("userInfo", gitHubUserDTO);

                        break;
                    }

                }
            }

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
