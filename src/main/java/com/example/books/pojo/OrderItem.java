package com.example.books.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 
 * @TableName t_order_item
 */
@TableName(value ="t_order_item")
@Data
public class OrderItem implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private Integer book;

    @TableField(exist = false)
    private Book bookItem;

    /**
     *
     */
    private Integer buycount;

    /**
     *
     */
    private Integer orderbean;

    /**
     *
     */
    private String orderno;

    @TableField(exist = false)
    private Double sub;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public OrderItem(Integer book, Integer buycount, Integer orderbean, String orderno) {
        this.book = book;
        this.buycount = buycount;
        this.orderbean = orderbean;
        this.orderno = orderno;
    }

    /**
     * 計算小計(價格*數量)
     */
    public Double getSub() {
        BigDecimal bigDecimalPrice = new BigDecimal(getBookItem().getPrice() + "");
        BigDecimal bigDecimalBuyCount = new BigDecimal("" + buycount);
        BigDecimal bigDecimalSub = bigDecimalPrice.multiply(bigDecimalBuyCount);
        sub = bigDecimalSub.doubleValue();
        return sub;
    }
}