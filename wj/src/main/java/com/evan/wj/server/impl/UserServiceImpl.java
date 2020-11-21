package com.evan.wj.server.impl;

import com.evan.wj.mapper.UserMapper;
import com.evan.wj.pojo.User;
import com.evan.wj.server.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectUserByName(String username) {
        return userMapper.selectUserByName(username);
    }
}
