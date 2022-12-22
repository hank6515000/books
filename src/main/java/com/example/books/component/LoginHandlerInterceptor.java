package com.example.books.component;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("loginUser");
        if (user == null){
            //未登入 ,轉發到登入頁面
            request.setAttribute("msg","沒有訪問權限");
            request.getRequestDispatcher("/index.html").forward(request,response);
            return false;
        }else {
            //已登入 放行請求
            return true;
        }
    }

}
