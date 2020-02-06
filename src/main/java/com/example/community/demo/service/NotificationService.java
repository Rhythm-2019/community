package com.example.community.demo.service;

import com.example.community.demo.dto.NotificationDTO;
import com.example.community.demo.dto.PaginationDTO;
import com.example.community.demo.enums.NotificationStatusEnum;
import com.example.community.demo.enums.NotificationTypeEnum;
import com.example.community.demo.exception.CustomizeErrorCode;
import com.example.community.demo.exception.CustomizeException;
import com.example.community.demo.mapper.NotificationMapper;
import com.example.community.demo.mapper.QuestionMapper;
import com.example.community.demo.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired(required = false)
    private NotificationMapper notificationMapper;

    @Autowired(required = false)
    private QuestionMapper questionMapper;

    public PaginationDTO list(Integer userId, Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        //这里由于需要一些处理，所以不直接set了
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        example.setOrderByClause("GMT_CREATE DESC");
        Integer totalCount = (int) notificationMapper.countByExample(example);
        paginationDTO.setPagination(totalCount,page,size);
        //先查一下question表，返回一个列表
        NotificationExample example1 = new NotificationExample();
        example1.createCriteria().andReceiverEqualTo(userId);
        example1.setOrderByClause("GMT_CREATE DESC");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example1, new RowBounds(page,size));
        if(notifications.size() == 0){
            return paginationDTO;
        }
        //新建负责组装两个表格的dto列表
        List<NotificationDTO> notificationDTOS = new ArrayList<NotificationDTO>();

        //Set<Integer> userId = notifications.stream().map(notification -> notification.getNotifier()).collect(Collectors.toSet());

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            //类型需要在这里转一下
            notificationDTO.setTypeName(NotificationTypeEnum.nameOf(notification.getType()));

            notificationDTOS.add(notificationDTO);
        }


        paginationDTO.setDataDTOS(notificationDTOS);
        return paginationDTO;
    }

    public Integer countUnread(Integer id) {

        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(id).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        Integer unRead = (int)notificationMapper.countByExample(example);

        return unRead;

    }

    public NotificationDTO read(Integer id, Integer userId) {

        Notification notification = notificationMapper.selectByPrimaryKey(id);
        //判断有没有这个消息
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        //检查一下是不是你的通知
        if(notification.getReceiver() != userId){
            throw new CustomizeException(CustomizeErrorCode.NO_ALLOW_READ);
        }
        //查出问题的id
        Question question = questionMapper.selectByPrimaryKey(notification.getOuterId());

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setOuterId(question.getId());

        //修改一下未读状态
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        return notificationDTO;
    }
}
