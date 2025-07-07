package com.dunjia.back.controller;

import com.dunjia.back.pojo.Result;
import com.dunjia.back.pojo.User;
import com.dunjia.back.service.UserService;
import com.dunjia.back.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        log.info("登录请求：{}", user);

        User userDB = userService.login(user);

        // 登录成功 -> 生成 jwt
        if (userDB != null) {
            String jwt = JwtUtils.generateJwt();
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("user", userDB);  // Add user object to the map
            responseData.put("token", jwt);      // Add JWT to the map

            return Result.success(responseData);
        }

        // 登录失败
        return Result.error("用户名或密码错误");
    }
}
