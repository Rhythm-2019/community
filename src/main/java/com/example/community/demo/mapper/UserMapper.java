package com.example.community.demo.mapper;

import com.example.community.demo.model.UserModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into user(account_id,name,token,gmt_create,gmt_modified,avatar_url) " +
            "values(#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
     void insert(UserModel user);

    @Select("select * from user where token = #{token}")
    UserModel findByToken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    UserModel findById(@Param("id") Integer id);

    @Select("select * from user where account_id = #{accountId}")
    UserModel findByAccountId(@Param("accountId") Integer accountId);

    @Select("update user set name = #{name}, token = #{token}, avatar_url = #{avatarUrl}, gmt_modified = #{gmtModified}" +
            " where account_id = #{accountId}")
    void update(UserModel userModel);
}
