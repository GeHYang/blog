package com.yang.blog.chat.entity.pojo;

import java.util.Date;
import com.yang.blog.pojo.entity.BaseEntity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.*;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 聊天表(Chat)表实体类
 *
 * @author makejava
 * @since 2023-05-24 15:28:16
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("chat")
@ApiModel(description = "聊天表")
public class Chat extends BaseEntity implements Serializable {
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
	@ApiModelProperty(name = "unread", value = "未读：0未读，1已读")
    private Integer unread;

    //消息类型：text文本，voice语音	
	@ApiModelProperty(name = "type", value = "消息类型：text文本，voice语音")
    private String type;
}
