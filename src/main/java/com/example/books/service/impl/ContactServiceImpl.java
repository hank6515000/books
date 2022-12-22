package com.example.books.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.books.pojo.Contact;
import com.example.books.service.ContactService;
import com.example.books.mapper.ContactMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【t_contact】的数据库操作Service实现
* @createDate 2022-12-21 14:00:48
*/
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact>
    implements ContactService{

}




