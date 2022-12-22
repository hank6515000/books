package com.example.books.service;

import com.example.books.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Lenovo
* @description 针对表【t_user】的数据库操作Service
* @createDate 2022-12-16 09:12:49
*/
public interface UserService extends IService<User> {

    User getUserByUserName(String username);

}
