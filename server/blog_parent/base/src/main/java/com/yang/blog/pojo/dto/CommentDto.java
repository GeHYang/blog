package com.yang.blog.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Yang
 * @create: 2023-05-16
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "评论请求")
public class CommentDto {
    //评论id
    @ApiModelProperty(value = "评论id：忽略", example = "1")
    @TableId
    private Long id;
    // 动态id
    @ApiModelProperty(value = "动态id", example = "1")
    private Long dynamicId;
    //父评论id
    @ApiModelProperty(value = "父评论id：可以为空", example = "1")
    private Long pid;
    //评论内容
    @ApiModelProperty(value = "评论内容", example = "1")
    private String content;
    //评论类型：0消息
    @ApiModelProperty(value = "评论类型,默认：0消息", example = "0")
    private Integer type = 0;
    //回复的评论id
    @ApiModelProperty(value = "回复的评论id", example = "1")
    private Long replyId;
    //@的人的id
    @ApiModelProperty(value = "@的人的id", example = "1")
    private Long remindUserId;
}
