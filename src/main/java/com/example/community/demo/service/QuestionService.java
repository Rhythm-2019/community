package com.example.community.demo.service;

import com.example.community.demo.dto.PaginationDTO;
import com.example.community.demo.dto.QuestionDTO;
import com.example.community.demo.mapper.QuestionMapper;
import com.example.community.demo.mapper.UserMapper;
import com.example.community.demo.model.*;
import org.apache.ibatis.session.RowBounds;
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
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());
        paginationDTO.setPagination(totalCount,page, size);
        //先查一下question表，返回一个列表
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(size * (paginationDTO.getPage() - 1), size));
        //新建负责组装两个表格的dto列表
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();
        for (Question question : questionList) {
            QuestionDTO questionDTO = new QuestionDTO();
            //先查user
            User user= userMapper.selectByPrimaryKey(question.getCreator());
            questionDTO.setUser(user);
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
        QuestionExample example = new QuestionExample();
        example.createCriteria().andIdEqualTo(userId);
        Integer totalCount =(int) questionMapper.countByExample(example);
        paginationDTO.setPagination(totalCount,page,size);
        //先查一下question表，返回一个列表
        QuestionExample example1 = new QuestionExample();
        example.createCriteria().andIdEqualTo(userId);
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(example1, new RowBounds());
        //新建负责组装两个表格的dto列表
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();
        for (Question question : questionList) {
            QuestionDTO questionDTO = new QuestionDTO();
            //先查user
            User user= userMapper.selectByPrimaryKey(question.getCreator());
            questionDTO.setUser(user);
            //再把questionModel复制到questionDTO里面，这里不用一个一个set了
            BeanUtils.copyProperties(question,questionDTO);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestionDTOS(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user= userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null){
            //创建
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setViewCount(0);
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else{
            //更新
            question.setGmtCreate(System.currentTimeMillis());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(question,questionExample);
        }
    }
}
