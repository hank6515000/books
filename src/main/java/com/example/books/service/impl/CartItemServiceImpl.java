package com.example.books.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.books.mapper.BookMapper;
import com.example.books.mapper.UserMapper;
import com.example.books.pojo.Book;
import com.example.books.pojo.Cart;
import com.example.books.pojo.CartItem;
import com.example.books.pojo.User;
import com.example.books.service.CartItemService;
import com.example.books.mapper.CartItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author Lenovo
* @description 针对表【t_cart_item】的数据库操作Service实现
* @createDate 2022-12-17 20:23:09
*/
@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem>
    implements CartItemService{

    @Autowired(required = false)
    CartItemMapper cartItemMapper;

    @Autowired(required = false)
    BookMapper bookMapper;

    @Autowired(required = false)
    UserMapper userMapper;

    public void addCartItem(CartItem cartItem){
               //set cartItem
        cartItem = new CartItem(cartItem.getBook(), cartItem.getBuycount(),cartItem.getUserbean());
        cartItemMapper.insert(cartItem);
    }

    public void updateCartItem(CartItem cartItem){
        UpdateWrapper<CartItem>updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("buyCount",cartItem.getBuycount()).eq("id",cartItem.getId());
        cartItemMapper.update(cartItem,updateWrapper);
    }


    /**
     * 將CartItem中的id轉換成為Book類實例
     */
    public List<CartItem> getCartItemList(User user){
        QueryWrapper<CartItem>queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userbean",user.getId());
        List<CartItem> cartItemList = cartItemMapper.selectList(queryWrapper);
        for (CartItem cartItem : cartItemList){
            Book book = bookMapper.selectById(cartItem.getBook());
            cartItem.setBookItem(book);
            cartItem.getSub();
        }
        return cartItemList;
    }

    /**
     * 將cartItem中的book類實例賦值給cart
     */
    public Cart getCart(User user){
        List<CartItem> cartItemList = getCartItemList(user);
        Map<Integer,CartItem>cartItemMap = new HashMap<>();
        for (CartItem cartItem :cartItemList){
            cartItemMap.put(cartItem.getBook(),cartItem);
        }
        Cart cart = new Cart();
        cart.setCartItemMap(cartItemMap);
        cart.getTotal();
        cart.getTotalCount();
        cart.getTotalBookCount();
        return cart;
    }

    /**
     * 判斷新增或修改cartItem操作
     * 有相同書籍新增 沒有則修改(主要修改buycount)
     */
    public void addOrUpdateCartItemCartItem(CartItem cartItem , Cart cart){
        if (cart != null){
            Map<Integer,CartItem>cartItemMap = cart.getCartItemMap();
            if (cartItemMap == null){
                cartItemMap = new HashMap<>();
            }
            //如果有此本圖書時,先找到cart中對應的書籍資料,再將cartItem中的buyCount加上cart中的buyCount
            if (cartItemMap.containsKey(cartItem.getBook())){
                System.out.println("有進來");
                CartItem cartItemByCart = cartItemMap.get(cartItem.getBook());
                //把cartItem中的buyCount加上cart中的buyCount
                cartItemByCart.setBuycount(cartItemByCart.getBuycount()+cartItem.getBuycount());
                //更新圖書中的buyCount
                updateCartItem(cartItemByCart);

            }else {
                System.out.println("直接新增");
                //沒有則直接新增書籍
               addCartItem(cartItem);
            }
            }else{
            System.out.println("直接新增(最外圈)");
            //直接增加圖書
               addCartItem(cartItem);
        }
    }

    @Override
    public void removeAllByUid(Integer uid) {
        QueryWrapper<CartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userbean",uid);
        cartItemMapper.delete(queryWrapper);
    }

}




