package com.example.books.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_order
 */
@TableName(value ="t_order")
@Data
public class Order implements Serializable {
    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 訂單編號
     */
    private String orderno;

    /**
     * 下單日期
     */
    private Date orderdate;

    /**
     * 下單用戶
     */
    private Integer orderuser;

    /**
     * 訂單金額
     */
    private Double ordermoney;

    /**
     * 訂單狀態
     */
    private Integer orderstatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}