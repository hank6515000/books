package com.example.books.controller;

import com.example.books.pojo.*;
import com.example.books.service.CartItemService;
import com.example.books.service.OrderItemService;
import com.example.books.service.OrderService;
import com.example.books.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.*;

@Controller
public class OrderController {

    @Autowired
    CartItemService cartItemService;

    @Autowired
    OrderItemService orderItemService;
    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    private User getUser(Principal principal){
        String username = principal.getName();
        User user = userService.getUserByUserName(username);
        return user;
    }

    /**
     * checkout操作
     * 1.執行後賦值給orderItem 2.將order賦值添加到數據庫中
     */
    @ResponseBody
    @PostMapping("/checkout")
    public Msg checkout(Principal principal , HttpSession session){
        User user = getUser(principal);
        Order order = new Order();
        order.setOrderuser(user.getId());

        Calendar now = Calendar.getInstance();
        String year = String.valueOf(now.get(Calendar.YEAR));
        String month = String.valueOf(now.get(Calendar.MONTH)+1);
        String day = String.valueOf(now.get(Calendar.DATE));
        String hour = String.valueOf(now.get(Calendar.HOUR));
        String min = String.valueOf(now.get(Calendar.MINUTE));
        String sec = String.valueOf(now.get(Calendar.SECOND));
        String dateNow = year+month+day+hour+min+sec ;

        order.setOrderno(UUID.randomUUID()+dateNow);
        order.setOrderdate(new Date());
        Cart cart = (Cart) session.getAttribute("cart");
        System.out.println(cart);
        order.setOrdermoney(cart.getTotal());
        order.setOrderstatus(0);

        orderService.addOrderItem(user,order);

        return Msg.success();
    }

    @ResponseBody
    @PostMapping("/getOrder")
    public Msg getOrder(Principal principal){
        User user = getUser(principal);
        List<Order> orderList = orderService.getOrderList(user.getId());
        return Msg.success().add("orderList",orderList);
    }

    @ResponseBody
    @PostMapping("/getOrderItem")
    public Msg getOrderItem(@RequestParam("orderNo")String orderNo){
        List<OrderItem> orderItemList = orderItemService.getOrderItemList(orderNo);
        return Msg.success().add("orderItemList",orderItemList);
    }

}
