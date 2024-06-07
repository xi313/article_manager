package com.example.service;

import com.example.pojo.User;

public interface UserService {
    //根据用户名查找用户
    User findByUserName(String username);
    //注册
    void register(String username, String password);
    //更新
    void update(User user);
    //更新代码
    void updatePwd(String newPwd);
}
