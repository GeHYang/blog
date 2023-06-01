package com.yang.blog.chat.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yang.blog.pojo.vo.UserVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: Yang
 * @create: 2023-05-26
 * @Description: 聊天列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatListVo implements Serializable {

    private Long toId;
    private UserVo user;
    private String lastMsg;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "Asia/Shanghai")
    private Date lastMsgTime;
    private Integer unread;

}
