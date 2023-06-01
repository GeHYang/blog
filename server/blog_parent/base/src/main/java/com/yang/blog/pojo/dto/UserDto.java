package com.yang.blog.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Yang
 * @create: 2023-04-26
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("请求用户信息数据对象")
public class UserDto {
    //用户ID
    @ApiModelProperty(value = "用户ID", example = "1")
    @TableId
    private Long id;
    //用户名
    @ApiModelProperty("用户名")
    private String username;
    //密码
    @ApiModelProperty("密码")
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
    // 验证码
    @ApiModelProperty(value = "验证码", example = "1")
    private String code;
}
