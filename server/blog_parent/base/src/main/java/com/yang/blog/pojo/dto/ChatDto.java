package com.yang.blog.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Yang
 * @create: 2023-05-24
 * @Description: 聊天
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto implements Serializable {
    //聊天ID
    @TableId
    private Long id;

    //聊天内容
    @ApiModelProperty(name = "content", value = "聊天内容")
    private String content;

    //接收者id
    @ApiModelProperty(name = "recvId", value = "接收者id")
    private Long recvId;

    //未读
    @ApiModelProperty(name = "unread", value = "未读")
    private Integer unread;

    //消息类型：text文本，voice语音
    @ApiModelProperty(name = "type", value = "消息类型：text文本，voice语音")
    private String type;
}
