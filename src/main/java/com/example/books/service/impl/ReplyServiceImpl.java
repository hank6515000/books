package com.example.books.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.books.mapper.UserMapper;
import com.example.books.pojo.Reply;
import com.example.books.pojo.User;
import com.example.books.service.ReplyService;
import com.example.books.mapper.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author Lenovo
* @description 针对表【t_reply】的数据库操作Service实现
* @createDate 2022-12-22 13:14:14
*/
@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply>
    implements ReplyService{


    @Autowired(required = false)
    ReplyMapper replyMapper;

    @Autowired(required = false)
    UserMapper userMapper;

    public void addReply(User user , Integer bid , String replyItem , Integer starNum){
        Reply reply = new Reply();
        reply.setReplyuser(user.getId());
        reply.setBookid(bid);
        reply.setReply(replyItem);
        reply.setStarnum(starNum);
        reply.setReplydate(new Date());
        replyMapper.insert(reply);
    }

    public List<Reply> getReplyList(Integer bid){
        QueryWrapper<Reply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bookId",bid);
        List<Reply> replyList = replyMapper.selectList(queryWrapper);
        System.out.println(replyList);
        for (Reply reply : replyList){
            User user = userMapper.selectById(reply.getReplyuser());
            reply.setUserInfo(user);
        }
        return replyList;
    }
}




