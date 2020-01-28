package com.example.community.demo.provider;

import com.alibaba.fastjson.JSON;
import com.example.community.demo.dto.AccesstokenDTO;
import com.example.community.demo.dto.GitHubUserDTO;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccesstoken(AccesstokenDTO accesstokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accesstokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String token = response.body().string().split("&")[0].split("=")[1];
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GitHubUserDTO getUserInfo(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            GitHubUserDTO gitHubUserDTO = JSON.parseObject(response.body().string(), GitHubUserDTO.class);
            return gitHubUserDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
