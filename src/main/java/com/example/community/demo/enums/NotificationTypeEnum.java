package com.example.community.demo.enums;

public enum  NotificationTypeEnum {

    COMMENT(1,"评论了您的提问"),
    REPLY(2,"回复了您的评论"),
    LIKE(3,"点赞了您的评论");

    private int type;
    private String typeName;

    NotificationTypeEnum(int type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public static String nameOf(Integer type) {

        for (NotificationTypeEnum e : NotificationTypeEnum.values()) {
            if(e.getType() == type){
                return e.getTypeName();
            }
        }

        return null;


    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }
}
