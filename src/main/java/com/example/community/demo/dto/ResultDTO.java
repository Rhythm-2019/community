package com.example.community.demo.dto;

import com.example.community.demo.exception.CustomizeErrorCode;
import com.example.community.demo.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO<T> {
    private String message;
    private Integer code;
    private T data;


    public static ResultDTO errorOf(Integer code, String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.message = message;
        resultDTO.code = code;
        return resultDTO;

    }

    public static ResultDTO errorOf(CustomizeErrorCode noLogin) {
        return errorOf(noLogin.getCode(), noLogin.getMessage());
    }

    public static <T> ResultDTO okOf(T t) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.message = "请求成功";
        resultDTO.code = 200;
        resultDTO.data = t;
        return resultDTO;

    }
    public static  ResultDTO okOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.message = "请求成功";
        resultDTO.code = 200;
        return resultDTO;

    }

    public static ResultDTO errorOf(CustomizeException e) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.message = e.getMessage();
        resultDTO.code = e.getCode();
        return resultDTO;
    }
}
