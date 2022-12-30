package com.example.books.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_reply
 */
@TableName(value ="t_reply")
@Data
public class Reply implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer bookid;

    /**
     * 
     */
    private Integer replyuser;

    /**
     * 
     */
    private Integer starnum;

    /**
     * 
     */
    private String reply;

    /**
     * 
     */
    private Date replydate;

    @TableField(exist = false)
    private User userInfo;

    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}