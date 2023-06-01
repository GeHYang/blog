package com.yang.blog.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Yang
 * @create: 2023-04-23
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicVo {
    // 用户信息
    private UserVo user;
    //动态ID
    @TableId
    private Long id;
    //动态文本内容
    private String content;
    //动态图片
    private String[] imgs;
    //@的用户对应的id
    private String remindId;
    // 评论数量
    private Integer commentCount;
    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
