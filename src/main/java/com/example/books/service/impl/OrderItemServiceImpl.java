package com.example.books.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.books.mapper.BookMapper;
import com.example.books.mapper.OrderMapper;
import com.example.books.pojo.Book;
import com.example.books.pojo.Order;
import com.example.books.pojo.OrderItem;
import com.example.books.pojo.User;
import com.example.books.service.OrderItemService;
import com.example.books.mapper.OrderItemMapper;
import com.example.books.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【t_order_item】的数据库操作Service实现
* @createDate 2022-12-21 18:25:10
*/
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
    implements OrderItemService{

    @Autowired(required = false)
    OrderMapper orderMapper;

    @Autowired
    BookMapper bookMapper;
    @Autowired(required = false)
    OrderItemMapper orderItemMapper;
    @Override
    public List<OrderItem> getOrderItemList(String orderNo) {

        QueryWrapper<OrderItem>queryWrapperItem = new QueryWrapper<>();
        queryWrapperItem.eq("orderno",orderNo);
        List<OrderItem> orderItemList = orderItemMapper.selectList(queryWrapperItem);
        for (OrderItem orderItem :orderItemList){
            Book book = bookMapper.selectById(orderItem.getBook());
            orderItem.setBookItem(book);
            orderItem.getSub();
        }
        return orderItemList;
    }
}




