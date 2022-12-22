package com.example.books.service;

import com.example.books.pojo.User;
import com.example.books.pojo.Validate;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【pm_validate】的数据库操作Service
* @createDate 2022-12-14 10:08:43
*/
public interface ValidateService extends IService<Validate> {

    void sendPasswordResetEmail(SimpleMailMessage email);
    int insertNewResetRecord(Validate validate, User users, String token);

    int insertNewUserRecord(Validate validate ,String email , String token);
    List<Validate> findUserByResetToken(String resetToken);
    boolean validateLimitation(String email, long requestPerDay, long interval, String token);
    boolean sendValidateLimitation(String email, long requestPerDay, long interval);
}
