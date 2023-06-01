package com.yang.blog.pojo.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yang.blog.common.MyFieldSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: Yang
 * @create: 2023-04-23
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo implements Serializable {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final String staticPath = "http://192.168.0.4:32002/static";
    //用户ID
    @TableId
    private Long id;
    //用户名
    private String username;
    //昵称
    private String nickName;
    //用户头像
    private String avatar;
    //性别：1男，0女
    private Integer sex;
    //手机号
    @JsonSerialize(using = MyFieldSerialize.class)
    private String phoneNumber;
    //邮箱
    @JsonSerialize(using = MyFieldSerialize.class)
    private String email;
    //生日
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    //封面
    private String cover;

    public void setCover(String cover) {
        if(!cover.contains(staticPath))
            this.cover = staticPath + cover;
        else
            this.cover = cover;
    }

    public void setAvatar(String avatar) {
        if(!avatar.contains(staticPath))
            this.avatar = staticPath + avatar;
        else this.avatar = avatar;
    }
}
