package com.example.community.demo.service;

import com.example.community.demo.dto.PaginationDTO;
import com.example.community.demo.dto.QuestionDTO;
import com.example.community.demo.dto.QuestionQueryDTO;
import com.example.community.demo.exception.CustomizeErrorCode;
import com.example.community.demo.exception.CustomizeException;
import com.example.community.demo.mapper.QuestionExtMapper;
import com.example.community.demo.mapper.QuestionMapper;
import com.example.community.demo.mapper.UserMapper;
import com.example.community.demo.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(Integer page, Integer size, String keyword){
        Integer totalCount;
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        if(!(keyword == null || "".equals(keyword))){
            String[] split = keyword.split(" ");
            String keywordRex = Arrays.stream(split).collect(Collectors.joining("|"));
            totalCount = questionExtMapper.countBySearch(keywordRex);
            questionQueryDTO.setKeyword(keywordRex);
        }else{
            totalCount = questionExtMapper.count();
            keyword = null;
        }

        //封装一下
        PaginationDTO paginationDTO = new PaginationDTO();

        //这里由于需要一些处理，所以不直接set了
        paginationDTO.setPagination(totalCount,page, size);
        //先查一下question表，返回一个列表


        questionQueryDTO.setOffset(size * (paginationDTO.getPage() - 1));
        questionQueryDTO.setSize(size);
        List<Question> questionList = questionExtMapper.select(questionQueryDTO);
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

        paginationDTO.setDataDTOS(questionDTOList);
        return paginationDTO;
    };

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        //这里由于需要一些处理，所以不直接set了
        Integer totalCount = questionExtMapper.countById(userId);
        paginationDTO.setPagination(totalCount,page,size);
        //先查一下question表，返回一个列表
        //先查一下question表，返回一个列表
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setOffset(size * (paginationDTO.getPage() - 1));
        questionQueryDTO.setSize(size);
        List<Question> questionList = questionExtMapper.select(questionQueryDTO);
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

        paginationDTO.setDataDTOS(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NO_FOUND);
        }
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
            int updated = questionMapper.updateByExampleSelective(question, questionExample);
            if(updated != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NO_FOUND);
            }
        }
    }

    public void incView(Integer id) {

        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {

        if(questionDTO.getTag() == null || "".equals(questionDTO.getTag())){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NO_FOUND);
        }
        //可以这样写
        //questionDTO.getTag().replace(",","|")

        String[] tags = questionDTO.getTag().split(",");
        //拼接出正则表达式
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexpTag);
        //调用自定义的mapper
        List<Question> questions = questionExtMapper.selectTag(question);
        //把question转换为dto
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO1 = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO1);
            return questionDTO1;
        }).collect(Collectors.toList());
        return questionDTOS;

    }
}
