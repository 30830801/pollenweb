package com.pollen.pollenweb.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName(value="Users")

public class User implements  Serializable{
    //主键
    @TableField(value="id")
    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    //账号
    private String loginname;

    //用户名
    private String username;

    //密码
    private String password;

    //手机号
    private String phonenumber;

    //邮箱
    private String email;

    //标注
    private String note;

    //文件名
    private String filename;

    //创建时间
    private Date created_time;

    //最后登陆时间
    private Date last_login;
}
