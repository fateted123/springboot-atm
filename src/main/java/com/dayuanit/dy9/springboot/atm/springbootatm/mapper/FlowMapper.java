package com.dayuanit.dy9.springboot.atm.springbootatm.mapper;

import com.dayuanit.dy9.springboot.atm.springbootatm.entity.Flow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FlowMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Flow record);

    int insertSelective(Flow record);

    Flow selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Flow record);

    int updateByPrimaryKey(Flow record);

    List<Flow> listFlow(@Param("cardNum") String cardNum, @Param("offset") Integer offset, @Param("prePageNum") Integer prePageNum);

    int countFlow(@Param("cardNum") String cardNum);

    List<Flow> listTop10(@Param("userId") Integer userId);
}