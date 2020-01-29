package com.example.community.demo.service;

import com.example.community.demo.dto.GitHubUserDTO;
import com.example.community.demo.mapper.UserMapper;
import com.example.community.demo.model.User;
import com.example.community.demo.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public String createOrUpdateUser(GitHubUserDTO gitHubUserDTO) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(String.valueOf(gitHubUserDTO.getId()));
        List<User> users = userMapper.selectByExample(userExample);
        User user = new User();
        if(users.size() == 0){
            //没有就创建

            user.setAccountId(String.valueOf(gitHubUserDTO.getId()));
            user.setName(gitHubUserDTO.getLogin());
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(gitHubUserDTO.getAvatarUrl());
            userMapper.insert(user);

        }else{
            //有就更新
            user.setName(gitHubUserDTO.getLogin());
            user.setToken(UUID.randomUUID().toString());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(gitHubUserDTO.getAvatarUrl());
            UserExample example = new UserExample();
            example.createCriteria().andAccountIdEqualTo(String.valueOf(gitHubUserDTO.getId()));
            userMapper.updateByExampleSelective(user,example);
        }
        return user.getToken();
    }
}
