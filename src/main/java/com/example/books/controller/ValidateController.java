package com.example.books.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.books.pojo.Msg;
import com.example.books.pojo.User;
import com.example.books.pojo.Validate;
import com.example.books.service.UserService;
import com.example.books.service.ValidateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Controller
public class ValidateController {

    @Autowired
    private ValidateService validateService;

    @Autowired
    private UserService userService;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${server.port}")
    private String serverPort;
    /**
     * 發送忘記密碼郵件請求，每日申請次數不超過5次，每次申請間隔不低於1分鐘
     */
    @ResponseBody
    @ApiOperation(value = "發送忘記密碼郵件", notes = "發送忘記密碼郵件")
    @PostMapping("/sendValidationEmailForReset")
    public Msg sendValidationEmailForReset(@ApiParam("郵箱地址") @RequestParam("email") String email,
                                   HttpServletRequest request){
        QueryWrapper<User>queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",email);
        List<User> userList = userService.list(queryWrapper);
        if (userList == null){
            return Msg.fail().add("fail","用戶不存在");
        }else {
            if (validateService.sendValidateLimitation(email, 5,1)){
                // 若允許重置密碼，則在pm_validate表中插入一行數據，帶有token
                Validate validate = new Validate();
                validateService.insertNewResetRecord(validate, userList.get(0), UUID.randomUUID().toString());
                // 設置郵件內容
                String appUrl = request.getScheme() + "://" + request.getServerName()+ ":"+ serverPort;
                SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
                passwordResetEmail.setFrom(from);
                passwordResetEmail.setTo(email);
                passwordResetEmail.setSubject("【books線上書店】忘記密碼");
                passwordResetEmail.setText("您正在申請重置密碼，請點擊此鏈接重置密碼: \n" + appUrl + "/books/reset/reset?token=" + validate.getResetToken());
                validateService.sendPasswordResetEmail(passwordResetEmail);
                return Msg.success();
            }else {
               return Msg.fail().add("fail","請求過於頻繁");
            }
        }
    }

    /**
     * 將url的token和數據庫裏的token匹配，成功後便可修改密碼，token有效期爲60分鐘
     */
    @ResponseBody
    @ApiOperation(value = "重置密碼", notes = "重置密碼")
    @PostMapping("/resetPassword")
    public Msg resetPassword(@ApiParam("token") @RequestParam("token") String token,
                                              @ApiParam("密碼") @RequestParam("password") String password,
                                              @ApiParam("密碼確認") @RequestParam("confirmPassword") String confirmPassword){

        // 通過token找到validate記錄
        List<Validate> validates = validateService.findUserByResetToken(token);
        if (validates.isEmpty()){
           return Msg.fail().add("msg","該重置請求不存在");
        }else {
            Validate validate = validates.get(0);
            if (validateService.validateLimitation(validate.getEmail(), Long.MAX_VALUE, 60, token)){
                Integer userId = validate.getUserId();
                if (password.equals(confirmPassword)) {
                    String encodePassword = new BCryptPasswordEncoder(12).encode(password);
                    UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.set("password",encodePassword).eq("id",userId);
                    userService.update(updateWrapper);
                    return Msg.success();
                }else {
                    return Msg.fail().add("msg","確認密碼和密碼不一致，請重新輸入");
               }
            }else {
                return Msg.fail().add("msg","該鏈接失效");
            }
        }
    }

    /**
     * 發送註冊郵件請求，每日申請次數不超過5次，每次申請間隔不低於1分鐘
     */
    @ResponseBody
    @PostMapping("/sendValidationEmailForRegister")
    public Msg sendValidationEmailForRegister(@RequestParam("email") String email,
                                           HttpServletRequest request){
        QueryWrapper<User>queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",email);
        List<User> userList = userService.list(queryWrapper);
        System.out.println(userList);
        if (!userList.isEmpty()){
            return Msg.fail().add("msg","用戶已存在");
        }else {
            if (validateService.sendValidateLimitation(email, 5,1)){
                // 若允許重置密碼，則在pm_validate表中插入一行數據，帶有token
                Validate validate = new Validate();
                validateService.insertNewUserRecord(validate, email, UUID.randomUUID().toString());
                // 設置郵件內容
                String appUrl = request.getScheme() + "://" + request.getServerName()+ ":"+ serverPort;
                SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
                passwordResetEmail.setFrom(from);
                passwordResetEmail.setTo(email);
                passwordResetEmail.setSubject("【books線上書店】註冊帳號");
                passwordResetEmail.setText("您正在申請註冊，請點擊此鏈接開始註冊: \n" + appUrl + "/books/register/register?token=" + validate.getResetToken());
                validateService.sendPasswordResetEmail(passwordResetEmail);
                return Msg.success();
            }else {
                return Msg.fail().add("msg","請求過於頻繁");
            }
        }
    }

    /**
     * 將url的token和數據庫裏的token匹配，成功後便可註冊，token有效期爲60分鐘
     */
    @ResponseBody
    @PostMapping("/registerInfo")
    public Msg registerInfo(@RequestParam("token")String token, @RequestParam("name")String name,@RequestParam("password")String password
            ,@RequestParam("checkPassword")String checkPassword){
        // 通過token找到validate記錄
        List<Validate> validates = validateService.findUserByResetToken(token);
        if (validates == null){
            return Msg.fail().add("NotFound","該重置請求不存在");
        }else {
            Validate validate = validates.get(0);
            if (validateService.validateLimitation(validate.getEmail(), Long.MAX_VALUE, 60, token)){
              User user = new User();
              //再次認證姓名
              String regName = "((^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5}))";
              if (name.matches(regName)){
                  user.setName(name);
              }else {
                  return Msg.fail().add("msg","姓名輸入有誤");
              }
              //再次認證密碼
              String regPasswrd = "(^(?![0-9]+$)(?![a-zA-Z]+$)[0-9a-zA-Z]{8,16}$)";
              if (password.matches(regPasswrd) && password.equals(checkPassword)){
                  user.setPassword(new BCryptPasswordEncoder(12).encode(password));
              }else {
                  return Msg.fail().add("msg","密碼輸入有誤");
              }

              //從Validate中取得Email
              QueryWrapper<Validate>queryWrapper = new QueryWrapper<>();
              queryWrapper.select("email").eq("reset_token",token);
                Validate validateEmail = validateService.getOne(queryWrapper);
                String email = validateEmail.getEmail();
                user.setUsername(email);

                boolean save = userService.save(user);
                return Msg.success();
                }else {
                    return Msg.fail().add("msg","註冊失敗，請重新輸入");
                }
            }
        }



    @ResponseBody
    @PostMapping("/kaptchaValid")
    public Msg kaptchaValid(@RequestParam("verifyCode")String verifyCode, HttpSession session){
        Object kaptcha_session_key = session.getAttribute("KAPTCHA_SESSION_KEY");
        System.out.println(kaptcha_session_key);
        if (kaptcha_session_key ==null || !verifyCode.equals(kaptcha_session_key)){
            return Msg.fail().add("kaptMsg","圖形認證碼不一致");
        }else {
            return Msg.success().add("kaptMsg","驗證成功");
        }
    }
    }

