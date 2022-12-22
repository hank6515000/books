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
 * @TableName pm_validate
 */
@TableName(value ="pm_validate")
@Data
public class Validate implements Serializable {
    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用戶id
     */
    private Integer userId;

    /**
     * 信箱(用戶帳號)
     */
    private String email;

    /**
     * 重設的Token
     */
    private String resetToken;

    /**
     * 申請類型(reset,register)
     */
    private String type;

    /**
     * 申請時間
     */
    private Date gmtCreate;

    /**
     * 修改時間
     */
    private Date gmtModified;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}