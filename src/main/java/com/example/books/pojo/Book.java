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
 * @TableName t_book
 */
@TableName(value ="t_book")
@Data
public class Book implements Serializable {
    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 書籍圖片路徑
     */
    private String bookimg;

    /**
     * 
     */
    private String bookname;

    /**
     * 價格
     */
    private Double price;

    /**
     * 作者
     */
    private String author;

    /**
     * 銷售數量
     */
    private Integer salecount;

    /**
     * 庫存數量
     */
    private Integer bookcount;

    /**
     * 書籍類型
     */
    private Integer bookclass;

    /**
     * 出版社
     */
    private String publishinghouse;

    /**
     * 出版日
     */
    private Date publicatiodate;

    /**
     * 書籍介紹
     */
    private String content;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}