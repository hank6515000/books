package com.example.books.service;

import com.example.books.pojo.OrderItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.books.pojo.User;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【t_order_item】的数据库操作Service
* @createDate 2022-12-21 18:25:10
*/
public interface OrderItemService extends IService<OrderItem> {

    List<OrderItem> getOrderItemList(String orderNo);
}
