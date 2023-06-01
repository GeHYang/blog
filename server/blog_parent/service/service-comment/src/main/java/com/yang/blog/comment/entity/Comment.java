package com.yang.blog.comment.entity;

import com.yang.blog.pojo.entity.BaseEntity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Comment)表实体类
 *
 * @author makejava
 * @since 2023-05-16 11:56:45
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment")
public class Comment extends BaseEntity implements Serializable {
    //评论id    
    @TableId
    private Long id;
    // 动态id
    private Long dynamicId;
    //父评论id
    private Long pid;
    //评论内容
    private String content;
    //评论类型：0消息
    private Integer type;
    //回复的评论id
    private Long replyId;
    //@的人的id
    private Long remindUserId;
                        
}
