package com.example.books.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.books.pojo.Book;
import com.example.books.pojo.BookClass;
import com.example.books.service.BookClassService;
import com.example.books.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Controller
public class BookController {
    @Autowired
    BookService bookService;
    @Autowired
    BookClassService bookClassService;
    Page<Book> page ;

    /**
     * 瀏覽商品主頁
     */
    @GetMapping("/product/listing/bookClass/{bid}")
    public String classListing(@RequestParam(value = "pn", defaultValue = "1") Integer pn ,@PathVariable("bid") Integer bid,Model model , HttpSession session) {

            page = new Page<>(pn, 9);
            QueryWrapper <Book> queryWrapper = new QueryWrapper<>();
            Page<Book> bookPage;
            List<BookClass> bookClass = bookService.bookClass();
            session.setAttribute("bookClass", bookClass);
            if (bid == 0 ){
                queryWrapper.select("id","bookimg","bookname","price");
                bookPage = bookService.page(page, queryWrapper);
                model.addAttribute("books", bookPage);
                model.addAttribute("bid",bid);
            }else {
                queryWrapper.select("id","bookimg","bookname","price").like("bookClass",bid);
                bookPage = bookService.page(page,queryWrapper);
                model.addAttribute("books",bookPage);
                model.addAttribute("bid",bid);
            }
        return "product/product-listing";
    }


    /**
     * 單一商品瀏覽頁面
     */
    @GetMapping("/product/detail")
    public String detail(@RequestParam("bid") Integer bid , Model model){

        Book book = bookService.getById(bid);
        BookClass bookClassName = bookService.getBookClassName(bid);
        model.addAttribute("bookClassName",bookClassName);
        model.addAttribute("book",book);
        //相關書籍
        List<Book> relevantBooks = bookService.getRelevantBooks(book);
        model.addAttribute("relevantBooks",relevantBooks);
        return "product/product-detail";
    }

    /**
     *商品瀏覽操作
     */
    @GetMapping("/product/listing/sort/{operate}")
    public String sort(@RequestParam(value = "pn", defaultValue = "1") Integer pn,@PathVariable("operate") String operate ,Model model) {
        Page<Book> page = new Page<>(pn, 9);
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        Page<Book> bookPage = null;
        switch (operate) {
            case "desc":
                queryWrapper.orderByDesc("price");
                bookPage = bookService.page(page, queryWrapper);
                model.addAttribute("books", bookPage);
                break;
            case "asc":
                queryWrapper.orderByAsc("price");
                bookPage = bookService.page(page, queryWrapper);
                model.addAttribute("books", bookPage);
                break;
            case "new":
                queryWrapper.orderByDesc("publicatiodate");
                bookPage = bookService.page(page, queryWrapper);
                model.addAttribute("books", bookPage);
                break;
            case "hot":
                queryWrapper.orderByAsc("salecount");
                bookPage = bookService.page(page, queryWrapper);
                model.addAttribute("books", bookPage);
                break;
        }
            model.addAttribute("operate", operate);
            return "product/product-listing";
        }

    /**
     * 商品搜尋
     */
    @GetMapping("/product/listing/search")
    public String search(@RequestParam(value = "pn", defaultValue = "1") Integer pn,@RequestParam("keyWord")String keyWord,Model model){
        QueryWrapper <Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("bookname",keyWord).or().like("content",keyWord).or().like("author",keyWord);
        Page<Book>page = new Page<>(pn,9);
        Page<Book> bookPage = bookService.page(page, queryWrapper);
        model.addAttribute("books",bookPage);
        model.addAttribute("keyWord","keyWord");
        return "product/product-listing";
    }
}
