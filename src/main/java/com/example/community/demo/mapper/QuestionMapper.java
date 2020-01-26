package com.example.community.demo.mapper;

import com.example.community.demo.dto.QuestionDTO;
import com.example.community.demo.model.QuestionModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,comment_count,view_count,like_count) " +
            "values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{commentCount},#{viewCount},#{likeCount}," +
            "#{tag})")
    void createQuestion(QuestionModel QuestionModel);

    @Select("select * from question")
    List<QuestionModel> list();
}
