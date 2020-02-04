package com.example.community.demo.service;

import com.example.community.demo.dto.CommentDTO;
import com.example.community.demo.enums.CommentTypeEnum;
import com.example.community.demo.exception.CustomizeErrorCode;
import com.example.community.demo.exception.CustomizeException;
import com.example.community.demo.mapper.*;
import com.example.community.demo.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private QuestionExtMapper questionExtMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private CommentExtMapper commentExtMapper;

    @Transactional
    public void insertComment(Comment comment) {
        if( comment.getComment() == null || comment.getParentId() == null){
            //我觉得这种方式也行，可能是为了统一异常的处理才throw
            //return ResultDTO.errorOf(1001,"缺少参数！");
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NO_FOUND);
        }
        if( "".equals(comment.getComment())){
            throw new CustomizeException(CustomizeErrorCode.COMMENT_NO_VALUE);
        }
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.COMMENT_TYPE_WRONG);
        }

        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment comment1 = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(comment1 == null) {
                //找不到这一条评论
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NO_FOUND);
            }
            commentMapper.insert(comment);
            //添加阅读数
            Comment incComment = new Comment();
            incComment.setId(comment.getParentId());
            incComment.setCommentCount(1);
            commentExtMapper.incComment(incComment);

        }else{
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null) {
                //找不到这一条评论
                throw new CustomizeException(CustomizeErrorCode.COMMENT_QUESTION_NO_FOUND);
            }

            //需要引入事务
            commentMapper.insert(comment);
            //添加评论数
            question.setCommentCount(1);
            questionExtMapper.incComment(question);
        }
    }

    public List<CommentDTO> listByQuestionId(Integer id, CommentTypeEnum type) {
        //先查一下是否有这个问题
        //可以通过sql联表查询实现，这里用集合的方式实现
        //思路大概是这样子的
        //查一下所有的评论
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_modified desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if(comments.size() == 0){
            return new ArrayList<>();
        }
        //创建一个Set集合（可以去重），通过java 8的stream()或者传统的for循环吧commentaror拿出来
        //Set<Integer> userIds = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        //因为andIdIn说要传入继承List接口的实体，所以要抽出来
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Integer> userIds = new ArrayList<Integer>();
        userIds.addAll(commentators);
        //根据得到的所有id查出所有的用户
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        //吧users集合里面的id提出来，构成Map<id,user>
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        //然后把comment和对应的user组装成commentDTO返回
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            //把User和Comment都加进去
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());


        return commentDTOS;
    }
}
