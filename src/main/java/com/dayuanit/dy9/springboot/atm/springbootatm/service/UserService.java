package com.dayuanit.dy9.springboot.atm.springbootatm.service;

import com.dayuanit.dy9.springboot.atm.springbootatm.entity.User;
import com.dayuanit.dy9.springboot.atm.springbootatm.exception.BizException;
import com.dayuanit.dy9.springboot.atm.springbootatm.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    public void getUser(String name) {
        User user = userMapper.getUser(name);
        System.out.println(user.getUsername());
        user.setPwd("9");
        int rows = userMapper.updateByPrimaryKey(user);
        System.out.println("rows=" + rows);

    }

    public User login(String username, String pwd) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(pwd)) {
            throw new BizException("有必填参数");
        }

        final User user = userMapper.getUser(username);
        if (null == user) {
            throw new BizException("用户名不存在或者密码错误");
        }

        if (!pwd.equals(user.getPwd())) {
            throw new BizException("用户名不存在或者密码错误");
        }

        return user;
    }

    public User getUserById(int userId) {
        return userMapper.selectByPrimaryKey(userId);
    }
}
