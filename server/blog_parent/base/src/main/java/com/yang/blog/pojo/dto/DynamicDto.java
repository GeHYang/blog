package com.yang.blog.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Yang
 * @create: 2023-04-24
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicDto implements Serializable {

    //动态文本内容
    private String content;
    //动态图片
    private String[] imgs;
    //@的用户对应的id
    private String remindId;
    // 发布者id
//    private Long createBy;


}
