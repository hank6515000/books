package com.example.books.service;

import com.example.books.pojo.Book;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.books.pojo.BookClass;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
* @author Lenovo
* @description 针对表【t_book】的数据库操作Service
* @createDate 2022-12-07 10:34:09
*/
public interface BookService extends IService<Book> {

     List<BookClass> bookClass();

     void suggst(HttpSession session);

     BookClass getBookClassName(Integer bid);

     List<Book> getRelevantBooks(Book book);
}

