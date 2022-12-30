package com.example.books.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @TableName t_cart_item
 */
@Data
@TableName(value ="t_cart_item")
public class CartItem implements Serializable {
    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 購物車中書籍id
     */
    private Integer book;

    /**
     * 書籍資料(資料庫外)
     */
    @TableField(exist = false)
    private Book bookItem;
    /**
     * 購買數量
     */
    private Integer buycount;

    /**
     * 用戶id
     */
    private Integer userbean;

    /**
     * 小計
     */
    @TableField(exist = false)
    private Double sub;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @TableLogic
    private Integer isDeleted;


    public CartItem(Integer book, Integer buycount, Integer userbean) {
        this.book = book;
        this.buycount = buycount;
        this.userbean = userbean;
    }



    /**
     *計算小計(價格*數量)
     */
    public Double getSub() {
        BigDecimal bigDecimalPrice =new BigDecimal(getBookItem().getPrice()+"");
        BigDecimal bigDecimalBuyCount = new BigDecimal(""+buycount);
        BigDecimal bigDecimalSub =  bigDecimalPrice.multiply(bigDecimalBuyCount);
        sub = bigDecimalSub.doubleValue();
        return sub;
    }
}