package com.dayuanit.dy9.springboot.atm.springbootatm.controller;

import com.dayuanit.dy9.springboot.atm.springbootatm.dto.ResponseDTO;
import com.dayuanit.dy9.springboot.atm.springbootatm.entity.User;
import com.dayuanit.dy9.springboot.atm.springbootatm.exception.BizException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

public abstract class BaseController {

    @ExceptionHandler(BizException.class)
    public ResponseDTO processBizException(BizException be) {
        be.printStackTrace();
        return ResponseDTO.faild(be.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseDTO processException(Exception e) {
        e.printStackTrace();
        return ResponseDTO.faild("系统异常，请联系客服");
    }

    protected int getUserId(HttpSession session) {
//        Object object = session.getAttribute("loginUser");
//        if (object == null) {
//            throw new BizException("未登录");
//        }
//
//        User user = (User)object;
//        return user.getId();

        return 1;
    }
}
