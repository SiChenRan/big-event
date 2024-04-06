package com.edu.cqut.controller;


import com.edu.cqut.pojo.Result;
import com.edu.cqut.pojo.User;
import com.edu.cqut.service.UserService;
import com.edu.cqut.utils.JwtUtil;
import com.edu.cqut.utils.Md5Util;
import com.edu.cqut.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        //查询用户是否存在
        User u = userService.findByUserName(username);
        if (u == null) {
            userService.register(username, password);
            return Result.success();
        }
        return Result.error("用户名已被占用");
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        User loginUser = userService.findByUserName(username);
        if (loginUser == null) {
            return Result.error("用户名错误!");
        }

        if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
            //生成token
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token, token, 24, TimeUnit.HOURS);
            return Result.success(token);
        }

        return Result.error("密码错误!");
    }

    @GetMapping("/userInfo")
    public Result<User> getUserInfo(@RequestHeader(name = "Authorization") String token) {
        Map<String, Object> user = ThreadLocalUtil.get();
        Object username = user.get("username");
        return Result.success(userService.findByUserName(username.toString()));
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        userService.update(user);
        return Result.success();
    }


    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {//校验是否是url地址
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> param, @RequestHeader("Authorization") String token) {
        String oldPwd = param.get("old_pwd");
        String newPwd = param.get("new_pwd");
        String rePwd = param.get("re_pwd");
        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd))
            return Result.error("缺少必要的参数!");
        Map<String, Object> map = ThreadLocalUtil.get();
        User user = userService.findByUserName(map.get("username").toString());
        if (!Md5Util.getMD5String(oldPwd).equals(user.getPassword()))
            return Result.error("原密码错误!");
        if (Md5Util.getMD5String(newPwd).equals(user.getPassword()))
            return Result.error("新密码不可与旧密码一致");
        if (!newPwd.equals(rePwd))
            return Result.error("两次新密码不一致!");
        userService.updatePwd(Md5Util.getMD5String(newPwd));

        //在redis中删除对应的token

        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }
}
