package com.evan.wj.controller;

import com.evan.wj.pojo.User;
import com.evan.wj.result.Result;
import com.evan.wj.server.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.Objects;

@Slf4j
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @CrossOrigin//防止跨域攻击
    @PostMapping(value = "/api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser) {        //@RequestBody只能用POST方法,@RequestParam查询用GET方法，上传数据或者需要改变数据库中的值还是用POST
        // 对 html 标签进行转义，防止 XSS 攻击
        String username = requestUser.getUsername();
        String password = requestUser.getPassword();
        password = HtmlUtils.htmlEscape(password);
        username = HtmlUtils.htmlEscape(username);
        User user = userService.selectUserByName(username);
        if (user ==null || !Objects.equals(user.getUsername(), username) || !Objects.equals(user.getPassword(), password)) {
            return new Result(400);
        } else {
            return new Result(200);
        }
    }
}

