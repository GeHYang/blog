package com.yang.blog.user.pojo.entity;

import java.util.Date;
import com.yang.blog.pojo.entity.BaseEntity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2023-04-23 23:14:25
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User extends BaseEntity implements Serializable {
    //用户ID    
    @TableId
    private Long id;

    //用户名
    private String username;
        //密码
    private String password;
        //昵称
    private String nickName;
        //用户头像
    private String avatar;
        //性别：1男，0女
    private Integer sex;
        //手机号
    private String phoneNumber;
        //邮箱
    private String email;
        //生日
    private Date birthday;
        //封面
    private String cover;
        //用户状态：1正常，2封禁
    private Integer status;
                        
}
