package com.example.books.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.books.mapper.BookMapper;
import com.example.books.mapper.CartItemMapper;
import com.example.books.mapper.OrderItemMapper;
import com.example.books.pojo.*;
import com.example.books.service.CartItemService;
import com.example.books.service.OrderService;
import com.example.books.mapper.OrderMapper;
import com.example.books.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
* @author Lenovo
* @description 针对表【t_order】的数据库操作Service实现
* @createDate 2022-12-07 10:35:37
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

    @Autowired(required = false)
   OrderMapper orderMapper;

    @Autowired(required = false)
    OrderItemMapper orderItemMapper;

    @Autowired
    CartItemService cartItemService;

    @Autowired(required = false)
    BookMapper bookMapper;

    @Override
    public List<Order> getOrderList(Integer uid) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orderUser",uid);
        List<Order> orderList = orderMapper.selectList(queryWrapper);
        return orderList;
    }

    public void addOrderItem(User user,Order order){
        //1.訂單表添加一條訂單紀錄
        //2.訂單詳情添加購物車紀錄
        //3.購物車項目表中刪除對應的添加購物車紀錄
        //添加訂單紀錄
        orderMapper.insert(order);
        //添加購物車紀錄
        //根據用戶的購物車項目轉換為個別訂單項目
        Cart cart = cartItemService.getCart(user);


        Map<Integer, CartItem> cartItemMap = cart.getCartItemMap();
        for (CartItem cartItem : cartItemMap.values()){
            OrderItem orderItem =
                    new OrderItem(cartItem.getBook(),cartItem.getBuycount(),cartItem.getUserbean(),order.getOrderno());
            //查詢book
            Book book = bookMapper.selectById(cartItem.getBook());

            //book增加銷售量及減少庫存量
            UpdateWrapper<Book>updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("saleCount",book.getSalecount()+cartItem.getBuycount())
                            .set("bookCount",book.getBookcount()-cartItem.getBuycount())
                                    .eq("id",cartItem.getBook());
            bookMapper.update(book,updateWrapper);

            orderItemMapper.insert(orderItem);
        }
        //刪除購物車紀錄
        for (CartItem cartItem :cartItemMap.values()){
           cartItemService.removeById(cartItem.getId());
        }

    }
}




