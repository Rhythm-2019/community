package com.example.community.demo.interceptor;

import com.example.community.demo.dto.GitHubUserDTO;
import com.example.community.demo.mapper.UserMapper;
import com.example.community.demo.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired(required = false)
    private UserMapper userMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    //需要对token进行校验
                    UserModel userModel = userMapper.findByToken(token);
                    if(userModel != null){
                        GitHubUserDTO gitHubUserDTO = new GitHubUserDTO();
                        gitHubUserDTO.setLogin(userModel.getName());
                        gitHubUserDTO.setId(userModel.getId());
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
