package com.example.books.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.books.pojo.User;
import com.example.books.pojo.Validate;
import com.example.books.service.ValidateService;
import com.example.books.mapper.ValidateMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
* @author Lenovo
* @description 针对表【pm_validate】的数据库操作Service实现
* @createDate 2022-12-14 10:08:43
*/
@Service
public class ValidateServiceImpl extends ServiceImpl<ValidateMapper, Validate>
    implements ValidateService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ValidateMapper validateMapper;

    /**
     * 發送郵件：@Async進行異步調用發送郵件接口
     */
    @Override
    @Async
    public void sendPasswordResetEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }
    /**
     * 在pm_validate表中插入一條validate記錄，u
     * serid，email屬性來自pm_user表，token由UUID生成
     */
    @Override
    public int insertNewResetRecord(Validate validate, User users, String token) {
        validate.setUserId(users.getId());
        validate.setEmail(users.getUsername());
        validate.setResetToken(token);
        validate.setType("passwordReset");
        validate.setGmtCreate(new Date());
        validate.setGmtModified(new Date());
        return validateMapper.insert(validate);
    }

    @Override
    public int insertNewUserRecord(Validate validate, String email, String token) {
        validate.setEmail(email);
        validate.setResetToken(token);
        validate.setType("register");
        validate.setGmtCreate(new Date());
        validate.setGmtModified(new Date());
        return validateMapper.insert(validate);

    }

    /**
     * pm_validate表中，通過token查找重置申請記錄
     */
    @Override
    public List<Validate> findUserByResetToken(String resetToken) {
        QueryWrapper<Validate>queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("reset_token",resetToken);
        List<Validate> validates = validateMapper.selectList(queryWrapper);
        return validates;
    }


    /**
     * 驗證連接是否失效：鏈接有兩種情況失效
     * 1.超時
     * 2.最近請求的一次鏈接自動覆蓋之前的鏈接（待看代碼）
     */
    @Override
    public boolean validateLimitation(String email, long requestPerDay, long interval, String token) {
        QueryWrapper<Validate>queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email);
        List<Validate> validates = validateMapper.selectList(queryWrapper);
        //有紀錄會調用該函數,只需判斷是否超時
        Optional<Date> max = validates.stream().map(Validate::getGmtModified).max(Date::compareTo);
        Date dateOfLastReq = new Date();
        if (max.isPresent()) dateOfLastReq = max.get();

        long intervalForLastReq = new Date().getTime() - dateOfLastReq.getTime();

        Optional lastReqToken = validates.stream().filter(x -> x.getResetToken().equals(token)).map(Validate::getGmtModified).findAny();
        Date dateOfLastReqToken = new Date();
        if (lastReqToken.isPresent()){
            dateOfLastReqToken = (Date) lastReqToken.get();
        }
        return intervalForLastReq <= interval *60 *1000 && dateOfLastReq == dateOfLastReqToken;
    }
    /**
     * 驗證是否發送重置郵件：每個email的重置密碼
     * 每日請求上限爲requestPerDay次，
     * 與上一次的請求時間間隔爲interval分鐘。
     */
    @Override
    public boolean sendValidateLimitation(String email, long requestPerDay, long interval) {
        QueryWrapper<Validate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        List<Validate> validates = validateMapper.selectList(queryWrapper);
        //若無紀錄意味著是第一次申請,直接放行
        if (validates.isEmpty()) {
            return true;
        }
            //若有紀錄,則判定是否頻繁申請及是否達到每日請求上限
            long count = validates.stream().filter(x -> DateUtils.isSameDay(x.getGmtModified(), new Date())).count();
            Optional max = validates.stream().map(Validate::getGmtModified).max(Date::compareTo);
            Date dateOfLastReq = new Date();
            if(max.isPresent())dateOfLastReq = (Date) max.get();
            long intervalLastReq = new Date().getTime() - dateOfLastReq.getTime();
            return count <= requestPerDay && intervalLastReq >= interval * 60 * 1000;

    }
}




