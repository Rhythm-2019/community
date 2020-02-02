package com.example.community.demo.controller;

import com.example.community.demo.dto.CommentCreateDTO;
import com.example.community.demo.dto.GitHubUserDTO;
import com.example.community.demo.dto.ResultDTO;
import com.example.community.demo.exception.CustomizeErrorCode;
import com.example.community.demo.model.Comment;
import com.example.community.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        GitHubUserDTO userInfo = (GitHubUserDTO)request.getSession().getAttribute("userInfo");
        if(userInfo == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setComment(commentCreateDTO.getComment());
        comment.setType(commentCreateDTO.getType());
        comment.setCommentator(userInfo.getId());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setLikeCount(0);
        commentService.insertComment(comment);
        ResultDTO resultDTO = ResultDTO.okOf();
        return resultDTO;

    }
}
