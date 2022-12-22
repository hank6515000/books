package com.example.books.service;

import com.example.books.pojo.Reply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.books.pojo.User;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【t_reply】的数据库操作Service
* @createDate 2022-12-22 13:14:14
*/
public interface ReplyService extends IService<Reply> {

    void addReply(User user, Integer bid, String reply, Integer starNum);

    List<Reply> getReplyList(Integer bid);
}
