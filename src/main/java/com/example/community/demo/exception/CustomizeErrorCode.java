package com.example.community.demo.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NO_FOUND(2001,"该问题已不存在或被删除啦，换一个看看吧！"),
    TARGET_PARAM_NO_FOUND(2002,"缺少参数"),
    NO_LOGIN(2003,"用户未登录，不能评论"),
    SERVER_ERROR(2004,"服务器炸了，正在抢修您晚点再来吧！"),
    COMMENT_TYPE_WRONG(2005,"评论的类型参数有误，请检查一下！"),
    COMMENT_NO_FOUND(2005,"评论不存在，请重试！"),
    COMMENT_QUESTION_NO_FOUND(2006,"请求的问题不存在，请重试！"),
    COMMENT_NO_VALUE(2007,"评论不能为空，请重试！"),
    ;

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
