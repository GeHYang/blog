package com.yang.blog.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: Yang
 * @create: 2023-05-16
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "评论信息")
public class CommentVo implements Serializable {

    //评论id
    @ApiModelProperty(value = "评论id：忽略", example = "1")
    @TableId
    private Long id;
    // 评论人信息
    @ApiModelProperty(value = "评论人")
    private UserVo user;
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
    @ApiModelProperty(value = "回复的评论的发布者")
    private UserVo replyUser;
    //@的人的id
    @ApiModelProperty(value = "@的人的id", example = "1")
    private Long remindUserId;
    //创建时间
    @ApiModelProperty(value = "评论时间", example = "2023-05-16 13:05:03")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    // 二级评论
    @ApiModelProperty(value = "二级评论")
    private List<CommentVo> children;
}
