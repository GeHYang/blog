package com.yang.blog.dynamic.pojo.entity;

import com.yang.blog.pojo.entity.BaseEntity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Dynamic)表实体类
 *
 * @author makejava
 * @since 2023-04-23 17:11:30
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("dynamic")
public class Dynamic extends BaseEntity implements Serializable {
    //动态ID    
    @TableId
    private Long id;

    //动态文本内容
    private String content;
        //动态图片
    private String imgs;
        //@的用户对应的id
    private String remindId;
                        
}
