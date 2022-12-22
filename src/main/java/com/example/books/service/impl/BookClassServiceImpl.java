package com.example.books.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.books.pojo.BookClass;
import com.example.books.service.BookClassService;
import com.example.books.mapper.BookClassMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【t_book_class】的数据库操作Service实现
* @createDate 2022-12-08 11:19:22
*/
@Service
public class BookClassServiceImpl extends ServiceImpl<BookClassMapper, BookClass>
    implements BookClassService{

}




