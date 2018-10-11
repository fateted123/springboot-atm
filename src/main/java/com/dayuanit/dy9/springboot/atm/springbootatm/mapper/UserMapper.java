package com.dayuanit.dy9.springboot.atm.springbootatm.mapper;

import com.dayuanit.dy9.springboot.atm.springbootatm.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User getUser(@Param("username") String username);
}