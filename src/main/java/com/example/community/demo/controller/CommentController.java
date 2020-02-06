package com.example.community.demo.controller;

import com.example.community.demo.dto.CommentCreateDTO;
import com.example.community.demo.dto.CommentDTO;
import com.example.community.demo.dto.GitHubUserDTO;
import com.example.community.demo.dto.ResultDTO;
import com.example.community.demo.enums.CommentTypeEnum;
import com.example.community.demo.exception.CustomizeErrorCode;
import com.example.community.demo.model.Comment;
import com.example.community.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


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
        comment.setCommentCount(0);
        commentService.insertComment(comment,userInfo.getLogin());
        ResultDTO resultDTO = ResultDTO.okOf();
        return resultDTO;

    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO comments(@PathVariable(name = "id") Integer id){
        List<CommentDTO> commentDTOS = commentService.listByQuestionId(id, CommentTypeEnum.COMMENT);
        //需要对返回dto
        ResultDTO resultDTO = ResultDTO.okOf(commentDTOS);
        return resultDTO;
    }
}
