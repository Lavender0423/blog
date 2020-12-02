package com.lavender.blog.service;

import com.lavender.blog.po.User;

public interface UserService {
    // check username and password
    User checkUser(String username,String password);
}
