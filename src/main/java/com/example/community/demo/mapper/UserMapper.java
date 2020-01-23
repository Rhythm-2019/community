package com.example.community.demo.mapper;

import com.example.community.demo.dto.GitHubUser;
import com.example.community.demo.model.UserModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into user(account_id,name,token,gmt_create,gmt_modified) " +
            "values(#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified})")
     void insert(UserModel user);

    @Select("select * from user where token = #{token}")
    UserModel findByToken(@Param("token") String token);
}
