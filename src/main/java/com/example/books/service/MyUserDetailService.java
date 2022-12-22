package com.example.books.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.books.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地登入用戶Service
 */
@Service("userDetailService")
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = userService.getOne(queryWrapper);

        if (user == null){
            throw new UsernameNotFoundException("用戶名不存在");
        }
        //設定權限
        List<GrantedAuthority> auths =  AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_user");
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),auths);
    }

}
