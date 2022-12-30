package com.example.books.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName t_user
 */
@Data
@TableName(value ="t_user")
public class User implements Serializable {
    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用戶帳號(設為email)
     */
    private String username;

    /**
     * 密碼
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性別
     */
    private String sex;

    /**
     * 手機號碼
     */
    private String phone;

    /**
     * 頭像路徑
     */
    private String headimgPath;

    @TableField(exist = false)
    private Cart cart;

    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}