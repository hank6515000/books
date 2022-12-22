package com.example.books.OAth2;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.books.pojo.User;
import com.example.books.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 第三方登入成功後資料保存至數據庫
 */
@Component
public class OAth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        //將獲取到的使用者賦值給CustomerOAth2User
        CustomerOAth2User oAth2User = (CustomerOAth2User) authentication.getPrincipal();
        //把userName設為email 因為他會自動判定getName的值為使用者 因此必須將email設在getName中
        String userName = oAth2User.getName();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userName);

        User user = userService.getOne(queryWrapper);
        if (user == null){
            User oathUser = new User();
            //跟一般用戶登入相反 getName為email Username為姓名
            oathUser.setUsername(oAth2User.getName());
            oathUser.setName(oAth2User.getUsername());
            boolean b = userService.saveOrUpdate(oathUser);
            System.out.println("是否加入會員 :"+b);

        }else {
            System.out.println("已有會員資料");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
