package com.dayuanit.dy9.springboot.atm.springbootatm.mapper;

import com.dayuanit.dy9.springboot.atm.springbootatm.entity.Card;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Card record);

    int insertSelective(Card record);

    Card selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Card record);

    int updateByPrimaryKey(Card record);

    int modifyBalance(@Param("cardNum") String cardNum, @Param("balance")String balance, @Param("modifyTime")Date modifyTime);

    List<Card> listCard(@Param("userId") Integer userId, @Param("status") Integer status);

    Card getCard(@Param("cardNum") String cardNum);

    Card getCard4Lock(@Param("cardNum") String cardNum);

    int modifyBalance2(@Param("cardNum") String cardNum,
                       @Param("balance")String balance,
                       @Param("modifyTime")Date modifyTime,
                       @Param("oldVersion")int oldVersion,
                       @Param("newVersion") int newVersion);
}