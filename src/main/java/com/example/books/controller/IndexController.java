package com.example.books.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.books.OAth2.CustomerOAth2User;
import com.example.books.pojo.*;
import com.example.books.service.BookClassService;
import com.example.books.service.BookService;
import com.example.books.service.CartItemService;
import com.example.books.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    BookService bookService;

    @Autowired
    BookClassService bookClassService;

    @Autowired
    UserService userService;

    @Autowired
    CartItemService cartItemService;
    /**
     * 瀏覽首頁
     */
    @GetMapping(value = {"/index", "/"})
    public String index(Principal principal ,  Model model , HttpSession session) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.getUserByUserName(username);
            List<CartItem> cartItemList = cartItemService.getCartItemList(user);
            session.setAttribute("cartItemList",cartItemList);
            Cart cart = cartItemService.getCart(user);
            cart.getTotalBookCount();
            cart.getTotal();
            cart.getTotalBookCount();
            System.out.println(cart);
            session.setAttribute("cart",cart);
            session.setAttribute("user",user);
        }
            bookService.suggst(session);
            List<BookClass> bookClass = bookService.bookClass();
            session.setAttribute("bookClass", bookClass);
            QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id", "bookimg", "bookname", "price");
            Collection<Book> list = bookService.list(queryWrapper);
            model.addAttribute("books", list);
            return "index";

    }




}