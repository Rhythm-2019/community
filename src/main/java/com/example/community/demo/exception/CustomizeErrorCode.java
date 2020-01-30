package com.example.community.demo.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NO_FOUND("该问题已不存在或被删除啦，换一个看看吧！");
    private String message;

    CustomizeErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
