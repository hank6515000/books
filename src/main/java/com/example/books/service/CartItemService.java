package com.example.books.service;

import com.example.books.pojo.Cart;
import com.example.books.pojo.CartItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.books.pojo.User;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【t_cart_item】的数据库操作Service
* @createDate 2022-12-17 20:23:09
*/
public interface CartItemService extends IService<CartItem> {

     void addCartItem(CartItem cartItem);

    void updateCartItem(CartItem cartItem);

    List<CartItem> getCartItemList(User user);

    Cart getCart(User user);

    void addOrUpdateCartItemCartItem(CartItem cartItem , Cart cart);

    void removeAllByUid(Integer uid);


}
