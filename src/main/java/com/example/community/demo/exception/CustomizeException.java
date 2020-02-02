package com.example.community.demo.exception;

public class CustomizeException extends RuntimeException{

    private String message;
    private Integer code;

    public CustomizeException(Integer code,String message) {
        this.code = code;
        this.message = message;
    }

    public CustomizeException(CustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }


    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
