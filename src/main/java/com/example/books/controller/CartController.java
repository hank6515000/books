package com.example.books.controller;

import com.example.books.pojo.*;
import com.example.books.service.BookService;

import com.example.books.service.CartItemService;
import com.example.books.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;


@Controller
public class CartController {

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @Autowired
    CartItemService cartItemService;

    /**
     *取得用戶
     */
    public User getUser(Principal principal){
        String username = principal.getName();
        User user = userService.getUserByUserName(username);
        return user;
    }

    /**
     * 更新session中cart
     */
    public void setSessionCart(HttpSession session ,User user){
        List<CartItem> cartItemList = cartItemService.getCartItemList(user);
        session.setAttribute("cartItemList",cartItemList);
        Cart cart = cartItemService.getCart(user);
        session.setAttribute("cart",cart);
    }

    /**
     * 從member頁面中顯示購物車及從cartIndex跳轉頁面時的操作
     * @return
     */
    @ResponseBody
    @PostMapping("/cartInMember")
    public Msg cartInMember(HttpSession session , Principal principal){
        User user = getUser(principal);
        Cart cart = cartItemService.getCart(user);
        //將cart實例封裝到user當中
        user.setCart(cart);
        setSessionCart(session,user);
        return Msg.success().add("cart",user.getCart());
        }



    /**
     * 加入購物車操作
     */
    @ResponseBody
    @PostMapping("/addCartItem")
    public Msg addCartItem(HttpSession session , Principal principal, @RequestParam("bid")Integer bid, @RequestParam(value = "buyCount" ,defaultValue = "1") Integer buyCount) {
        if (principal == null) {
            return Msg.fail().add("msg", "請先登入");
        } else {
            User user = getUser(principal);
            Cart cart = cartItemService.getCart(user);
            //將cart實例封裝到user當中
            user.setCart(cart);
            CartItem cartItem = new CartItem(bid,buyCount,user.getId());
            cartItemService.addOrUpdateCartItemCartItem(cartItem,user.getCart());

            setSessionCart(session,user);
            return Msg.success().add("msg","已添加購物車");
        }
    }


    /**
     * 表單數量增減按鈕操作
     */
    @ResponseBody
    @PostMapping("/editCart")
    public Msg editCart(HttpSession session,Principal principal , @RequestParam("cartItemId") Integer cartItemId,@RequestParam("buyCount") Integer buyCount) {
        if (buyCount > 0) {
            CartItem cartItem = cartItemService.getById(cartItemId);
            cartItem.setBuycount(buyCount);
            cartItemService.updateCartItem(cartItem);
            User user = getUser(principal);
            setSessionCart(session, user);
        }
        return Msg.success();
    }

    /**
     *刪除商品操作
     */
    @ResponseBody
    @DeleteMapping("/deleteCart/{cartItemId}")
    public Msg deleteCart(HttpSession session ,Principal principal ,  @PathVariable("cartItemId")Integer cartItemId){
        User user = getUser(principal);
        setSessionCart(session, user);
        cartItemService.removeById(cartItemId);
        return Msg.success();
    }

    /**
     *刪除整筆商品操作
     */
    @ResponseBody
    @DeleteMapping("/deleteCart")
    public Msg deleteAll(HttpSession session , Principal principal){
        User user = getUser(principal);
        cartItemService.removeAllByUid(user.getId());
        setSessionCart(session, user);
        return Msg.success();
    }
}
