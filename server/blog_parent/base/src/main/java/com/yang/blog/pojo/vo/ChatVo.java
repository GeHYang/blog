package com.yang.blog.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: Yang
 * @create: 2023-05-24
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatVo implements Serializable {

    //聊天ID
    @TableId
    private Long id;

    //聊天内容
    @ApiModelProperty(name = "content", value = "聊天内容")
    private String content;

    //接收者
    @ApiModelProperty(name = "toUser", value = "对方信息")
    private UserVo toUser;

    //未读
    @ApiModelProperty(name = "unread", value = "未读：0未读，1已读")
    private Integer unread;

    //消息类型：text文本，voice语音
    @ApiModelProperty(name = "type", value = "消息类型：text文本，voice语音")
    private String type;

    //是否为自己的消息
    @ApiModelProperty(name = "isMe", value = "是否为自己的消息")
    private Boolean isMe = false;

    //消息时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "Asia/Shanghai")
    private Date createTime;
}
