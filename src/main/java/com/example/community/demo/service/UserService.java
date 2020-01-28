package com.example.community.demo.service;

import com.example.community.demo.dto.GitHubUserDTO;
import com.example.community.demo.mapper.UserMapper;
import com.example.community.demo.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    public String createOrUpdateUser(GitHubUserDTO gitHubUserDTO) {
        UserModel userModel = userMapper.findByAccountId(gitHubUserDTO.getId());
        if(userModel == null){
            //没有就创建
            userModel.setAccountId(String.valueOf(gitHubUserDTO.getId()));
            userModel.setName(gitHubUserDTO.getLogin());
            String token = UUID.randomUUID().toString();
            userModel.setToken(token);
            userModel.setGmtCreate(System.currentTimeMillis());
            userModel.setGmtModified(userModel.getGmtCreate());
            userModel.setAvatarUrl(gitHubUserDTO.getAvatarUrl());
            userMapper.insert(userModel);

        }else{
            //有就更新

            userModel.setName(gitHubUserDTO.getLogin());
            userModel.setToken(UUID.randomUUID().toString());
            userModel.setGmtModified(userModel.getGmtCreate());
            userModel.setAvatarUrl(gitHubUserDTO.getAvatarUrl());
            userMapper.update(userModel);
        }
        return userModel.getToken();
    }
}
