package com.dayuanit.dy9.springboot.atm.springbootatm.mapper;

import com.dayuanit.dy9.springboot.atm.springbootatm.entity.WxUserInfo;
import org.apache.ibatis.annotations.Param;

public interface WxUserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WxUserInfo record);

    int insertSelective(WxUserInfo record);

    WxUserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WxUserInfo record);

    int updateByPrimaryKey(WxUserInfo record);

    WxUserInfo getWxUserInfo(@Param("openid") String openid);
}