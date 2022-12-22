package com.example.books.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.books.pojo.Book;
import com.example.books.pojo.BookClass;
import com.example.books.service.BookClassService;
import com.example.books.service.BookService;
import com.example.books.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
* @author Lenovo
* @description 针对表【t_book】的数据库操作Service实现
* @createDate 2022-12-07 10:34:09
*/
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
    implements BookService{

    @Autowired
    BookClassService bookClassService;

    @Autowired
    BookMapper bookMapper;

    public List<BookClass> bookClass(){
        long listCount = bookClassService.count();
        List<BookClass> bookClasss = new ArrayList<>();

        for (int i = 1; i <= listCount; i++) {
            QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("bookClass", i);
            long bookCount = baseMapper.selectCount(queryWrapper);
            queryWrapper.select("bid", "bookClass");
            BookClass bookClass = bookClassService.getById(i);
            bookClass.setCount((int) bookCount);
            bookClasss.add(bookClass);
        }
        return bookClasss;
    }

    @Override
    public void suggst(HttpSession session) {
        QueryWrapper<Book>queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("salecount").last("limit 3");
        List<Book> books = baseMapper.selectList(queryWrapper);
        session.setAttribute("books",books);
    }

    public BookClass getBookClassName(Integer bid){
        QueryWrapper<Book>queryWrapper = new QueryWrapper<>();
        queryWrapper.select("bookClass").eq("id",bid);
        Book bookClass = bookMapper.selectOne(queryWrapper);
        BookClass bookClassName = bookClassService.getById(bookClass.getBookclass());
        return bookClassName;
    }

    public List<Book> getRelevantBooks(Book book){
        QueryWrapper<Book>queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","bookImg","bookName","price").eq("bookClass",book.getBookclass());
        List<Book> bookList = bookMapper.selectList(queryWrapper);

        return bookList;
    }
}







