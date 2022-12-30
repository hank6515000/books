package com.example.books.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.books.pojo.Contact;
import com.example.books.pojo.Msg;
import com.example.books.pojo.Reply;
import com.example.books.pojo.User;
import com.example.books.service.ContactService;
import com.example.books.service.ReplyService;
import com.example.books.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.security.Principal;
import java.util.List;
import java.util.UUID;


@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ContactService contactService;

    @Autowired
    ReplyService replyService;

    /**
     * 登入頁面
     */
    @GetMapping("/login")
    public String login(Principal principal){
        if (principal == null){
            return "login";
        }else {
            return "redirect:index";
        }

    }

    /**
     * 登出
     */
    @GetMapping("/logout")
    public String logout(){
        return "redirect:index";
    }

    /**
     * 發送重設密碼郵件頁面
     */
    @GetMapping("/reset/forget")
    public String forget(){
        return "reset/forget";
    }

    /**
     * 重設密碼頁面
     */
    @GetMapping("/reset/reset")
    public String reset(){
        return "reset/reset";
    }

    /**
     * 註冊頁面
     */
    @GetMapping("/register/register")
    public String register(){
        return "register/register";
    }

    /**
     * 發送註冊郵件
     */
    @GetMapping("/register/registerValid")
    public String registerValid(){
        return "register/registerValid";
    }

    /**
     * 會員頁面
     */
    @GetMapping("/member")
    public String member(Principal principal, Model model){
        if (principal == null){
            return "login";
        } else {
        String username = principal.getName();
        QueryWrapper<User>queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = userService.getOne(queryWrapper);

            model.addAttribute("user",user);
        }
        return "member";
    }

    /**
     *聯繫我頁面
     */
    @GetMapping("/contact")
    public String content() {
        return "contact";
    }

    /**
     * 取得用戶信息
     */
    @ResponseBody
    @PostMapping("/getUserInfo")
    public Msg getUserInfo(Principal principal){
        if (principal != null) {
            String username = principal.getName();
            User user = userService.getUserByUserName(username);
            return Msg.success().add("user",user);
        }else {
            return Msg.success();
        }
    }

    /**
     * 取得留言
     */
    @ResponseBody
    @PostMapping("/getContact")
    public Msg getContact(@RequestParam("name")String name , @RequestParam("email")String email ,@RequestParam("message")String message){
        Contact contact = new Contact(name,email,message);
        contactService.save(contact);
        return Msg.success();
    }


    /**
     * 增加回覆
     */
    @ResponseBody
    @PostMapping("/addReply")
    public Msg addReply(Principal principal,@RequestParam("bid")Integer bid ,@RequestParam("reply")String reply , @RequestParam("starNum")Integer starNum){
        String username = principal.getName();
        User user = userService.getUserByUserName(username);
        replyService.addReply(user,bid, reply, starNum);
        return Msg.success();
    }

    /**
     * 取得回覆
     */
    @ResponseBody
    @PostMapping("/getReply")
    public Msg getReply(@RequestParam("bid")Integer bid){
        List<Reply> replyList = replyService.getReplyList(bid);

        return Msg.success().add("replyList",replyList);
    }

    /**
     *發送修改密碼請求
     */
    @ResponseBody
    @PostMapping("/updatePwd")
    public Msg updatePwd(Principal principal, @RequestParam("password") String password ,@RequestParam("confirmPassword") String confirmPassword){
        String username = principal.getName();
        if (password.equals(confirmPassword)) {
            String encodePassword = new BCryptPasswordEncoder().encode(password);
            System.out.println(encodePassword);
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("password",encodePassword).eq("username",username);
            userService.update(updateWrapper);
            return Msg.success();
        }else {
            return Msg.fail().add("msg","確認密碼和密碼不一致，請重新輸入");
        }
    }

    /**
     *發送修改個人檔案請求
     */
    @ResponseBody
    @PostMapping("/updateProfile")
    public Msg update(@RequestParam("username")String username,@RequestParam("name")String name,
                      @RequestParam("phone")String phone, @RequestParam("sex")String sex) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userService.getOne(queryWrapper);
        user.setName(name);
        user.setPhone(phone);
        user.setSex(sex);
        boolean isUpdate = userService.saveOrUpdate(user);
        if (isUpdate) {
            return Msg.success().add("msg", "修改成功");
        } else {
            return Msg.fail().add("msg", "修改失敗，請重新輸入");
        }
    }


    /**
     *上傳頭像操作
     */
    @ResponseBody
    @PostMapping(value = {"/uploadImg"},consumes = {"multipart/form-data"})
    public Msg uploadImg(@RequestParam("headerImg")MultipartFile headerImg , Principal principal) throws IOException {
        String fileName ;
        if (headerImg.getSize() / 1000 > 100) {
            return Msg.fail().add("msg", "圖片大小不能超過100KB");
        } else {
            fileName = headerImg.getOriginalFilename();
            int index = fileName.lastIndexOf(".");
            String suffixName;
            if (index > 0) {
                suffixName = fileName.substring(fileName.lastIndexOf("."));
            } else {
                suffixName = ".png";
            }
            String username = principal.getName();
            User user = userService.getUserByUserName(username);
            fileName = user.getId() + suffixName;

            headerImg.transferTo(new File("C:\\HeaderImgs\\" + fileName));

            saveImg(username,fileName);
        }
        return Msg.success().add("path",fileName);
    }

    /**
     *保存圖片路徑方法
     */
    private boolean saveImg(String username , String path){
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("headImg_path",path).eq("username",username);
        boolean update = userService.update(updateWrapper);
        return update;
    }
}

