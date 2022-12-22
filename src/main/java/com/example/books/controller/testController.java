package com.example.books.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.books.pojo.Book;
import com.example.books.pojo.BookClass;
import com.example.books.pojo.Msg;
import com.example.books.pojo.User;
import com.example.books.service.BookService;
import com.example.books.service.UserService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class testController {
    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;
    @GetMapping("/test")
    public String test(){
        User user = userService.getById(25);

        return "/test/test";
    }


    @ResponseBody
    @GetMapping("/getData")
    public Msg getData(@RequestParam(value = "pn" , defaultValue = "1")Integer pn,Model model){
        Page<Book>page = new Page<>(pn,10);
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("bookimg","bookname","price");
        Page<Book>bookPage = bookService.page(page,queryWrapper);
        model.addAttribute("current",bookPage.getCurrent());
        model.addAttribute("pages",bookPage.getPages());
        model.addAttribute("total",bookPage.getTotal());
        List<BookClass> bookClass = bookService.bookClass();
        return Msg.success().add("page",bookPage).add("bookClass",bookClass);
    }



}
