package com.example.books.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_book_class
 */
@TableName(value ="t_book_class")
@Data
public class BookClass implements Serializable {
    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Integer bid;

    /**
     * 書籍類型
     */
    private String bookClass;

    /**
     * 類型總數(數據庫外屬性)
     */
    @TableField(exist = false)
    private Integer count;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}