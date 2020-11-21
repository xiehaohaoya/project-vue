package com.evan.wj.server;

import com.evan.wj.pojo.User;

public interface UserService {
    User selectUserByName(String username);
}
