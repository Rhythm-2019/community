package com.example.community.demo.service;

import com.example.community.demo.dto.PaginationDTO;
import com.example.community.demo.dto.QuestionDTO;
import com.example.community.demo.mapper.QuestionMapper;
import com.example.community.demo.mapper.UserMapper;
import com.example.community.demo.model.QuestionModel;
import com.example.community.demo.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private QuestionMapper questionMapper;

    public PaginationDTO list(Integer page, Integer size){
        PaginationDTO paginationDTO = new PaginationDTO();
        //这里由于需要一些处理，所以不直接set了
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount,page, size);
        //先查一下question表，返回一个列表
        List<QuestionModel> questionList = questionMapper.list(size*(paginationDTO.getPage()-1), size);
        //新建负责组装两个表格的dto列表
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();
        for (QuestionModel question : questionList) {
            QuestionDTO questionDTO = new QuestionDTO();
            //先查user
            UserModel userModel= userMapper.findById(question.getCreator());
            questionDTO.setUserModel(userModel);
            //再把questionModel复制到questionDTO里面，这里不用一个一个set了
            BeanUtils.copyProperties(question,questionDTO);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestionDTOS(questionDTOList);
        return paginationDTO;
    };

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        //这里由于需要一些处理，所以不直接set了
        Integer totalCount = questionMapper.countById(userId);
        paginationDTO.setPagination(totalCount,page,size);
        //先查一下question表，返回一个列表
        List<QuestionModel> questionList = questionMapper.listById(userId,size*(paginationDTO.getPage()-1), size);
        //新建负责组装两个表格的dto列表
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();
        for (QuestionModel question : questionList) {
            QuestionDTO questionDTO = new QuestionDTO();
            //先查user
            UserModel userModel= userMapper.findById(question.getCreator());
            questionDTO.setUserModel(userModel);
            //再把questionModel复制到questionDTO里面，这里不用一个一个set了
            BeanUtils.copyProperties(question,questionDTO);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestionDTOS(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        QuestionModel questionModel = questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(questionModel, questionDTO);
        UserModel userModel = userMapper.findById(questionModel.getCreator());
        questionDTO.setUserModel(userModel);
        return questionDTO;
    }

    public void createOrUpdate(QuestionModel questionModel) {
        if (questionModel.getId() == null){
            //创建
            questionModel.setCommentCount(0);
            questionModel.setLikeCount(0);
            questionModel.setViewCount(0);
            questionModel.setGmtCreate(System.currentTimeMillis());
            questionModel.setGmtModified(questionModel.getGmtCreate());
            questionMapper.createQuestion(questionModel);
        }else{
            //更新
            questionModel.setGmtCreate(System.currentTimeMillis());
            questionMapper.updateQuestion(questionModel);
        }
    }
}
