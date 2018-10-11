package com.dayuanit.dy9.springboot.atm.springbootatm.service;

import com.dayuanit.dy9.springboot.atm.springbootatm.entity.WxUserInfo;
import com.dayuanit.dy9.springboot.atm.springbootatm.exception.BizException;
import com.dayuanit.dy9.springboot.atm.springbootatm.mapper.WxUserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WxAuthenService {

    @Autowired
    private WxUserInfoMapper wxUserInfoMapper;

    public WxUserInfo getWxUserInfo(String openid) {
        final WxUserInfo wxUserInfo = wxUserInfoMapper.getWxUserInfo(openid);
        return wxUserInfo;
    }

    public void saveBindInfo(int userId, String openId, String nickName, String headUrl) {
        final WxUserInfo wxUserInfo = new WxUserInfo();
        wxUserInfo.setHeadimgurl(headUrl);
        wxUserInfo.setNickname(nickName);
        wxUserInfo.setOpenid(openId);
        wxUserInfo.setUserId(userId);
        int rows = wxUserInfoMapper.insert(wxUserInfo);
        if (1 != rows) {
            throw new BizException("绑定失败");
        }
    }
}
