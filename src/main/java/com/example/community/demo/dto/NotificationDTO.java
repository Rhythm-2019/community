package com.example.community.demo.dto;

import com.example.community.demo.model.User;
import lombok.Data;

@Data
public class NotificationDTO {

    private Long gmtCreate;
    private Integer status;
    private Integer notifier;
    private String notifierName;
    private String outerName;
    private String typeName;
    private Integer type;
    private Integer outerId;
    private Integer id;


}
