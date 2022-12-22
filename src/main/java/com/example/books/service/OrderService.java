package com.example.books.service;

import com.example.books.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.books.pojo.User;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【t_order】的数据库操作Service
* @createDate 2022-12-07 10:35:37
*/
public interface OrderService extends IService<Order> {

    List<Order> getOrderList(Integer uid);

    void addOrderItem(User user, Order order);

}
