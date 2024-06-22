package com.example.service.impl;

import com.example.pojo.User;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import com.example.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Resource
    UserService userService;


    @BeforeAll
    public static void init(){
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGFpbXMiOnsiaWQiOjAsInVzZXJuYW1lIjoidGVzdF91c2VyIn0sImV4cCI6MTcxOTExMTQ5N30.EEPalbshEoIWjhAy6YrrS1STo_Zgb6XW8FM8WFjC-6U";
        Map<String,Object> claims = JwtUtil.parseToken(token);
        ThreadLocalUtil.set(claims);
    }

    @Test
    User findByUserName() {
        User user = userService.findByUserName("test_user");
        System.out.println("findByUserName");
        System.out.println(user);
        return user;
    }

    @Test
    @Transactional
    @Rollback
    void register() {
        String Username = "test";
        String Password = "123456";
        userService.register(Username,Password);
        System.out.println("register");
        System.out.println(Username);
        System.out.println(Password);
    }

    @Test
    @Transactional
    @Rollback
    void update() {
        User user = findByUserName();
        user.setNickname("test");
        user.setEmail("123@163.com");
        userService.update(user);
        System.out.println("update");
        System.out.println(user);
    }

    @Test
    @Transactional
    @Rollback
    void updatePwd() {
        String newPwd = "123456778";
        userService.updatePwd(newPwd);
        System.out.println("updatePwd " + newPwd);
    }
}