package com.example.books.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_contact
 */
@TableName(value ="t_contact")
@Data
public class Contact implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    private String massage;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableLogic
    private Integer isDeleted;

    public Contact(String name, String email, String massage) {
        this.name = name;
        this.email = email;
        this.massage = massage;
    }
}