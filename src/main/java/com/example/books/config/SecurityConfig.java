package com.example.books.config;


import com.example.books.OAth2.CustomerOAth2UserService;
import com.example.books.OAth2.OAth2LoginSuccessHandler;
import com.example.books.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private CustomerOAth2UserService customerOAth2UserService;

    @Autowired
    private OAth2LoginSuccessHandler oAth2LoginSuccessHandler;

    /**
     * 注入數據源
     */
    @Autowired
    private DataSource dataSource;

    /**
     * 配置對象
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    /**
     * 登入解密操作
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService).passwordEncoder(password());
    }

    /**
     * 密碼加密
     */
    @Bean
    PasswordEncoder password() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 登入操作
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.logout().logoutUrl("/logout").logoutSuccessUrl("/index").permitAll();

        http.formLogin()
                .loginPage("/login")//自定義登入頁面
                .loginProcessingUrl("/checkUser") //設定登入頁面的Url
                .defaultSuccessUrl("/")//登入後導向
                .failureUrl("/login?error=true")//登入失敗後導向
                .and().authorizeRequests()
                .and()
                //第三方登入
                .oauth2Login()
                //自定義登入頁面
                .loginPage("/login")//登入後導向
                .defaultSuccessUrl("/")//登入後導向
                .userInfoEndpoint()
                .userService(customerOAth2UserService)
                .and()
                .successHandler(oAth2LoginSuccessHandler)
                .and()
//                記住用戶設置
                .rememberMe()
                .tokenRepository(persistentTokenRepository()) // 配置 token 持久化仓库
                .tokenValiditySeconds(3600) // remember 过期时间，单为秒
                .userDetailsService(myUserDetailService) // 处理自动登录逻辑
                .and().csrf().disable();



    }
}
