package com.example.community.demo.service;

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

    public List<QuestionDTO> list(){
        //先查一下question表，返回一个列表
        List<QuestionModel> questionList = questionMapper.list();
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
        return questionDTOList;
    };
}
