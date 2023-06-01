package com.yang.blog.chat.service;

import com.yang.blog.chat.entity.pojo.Chat;
import com.yang.blog.chat.entity.vo.ChatListVo;
import com.yang.blog.model.PageQuery;
import com.yang.blog.model.PageResult;
import com.yang.blog.model.Result;
import com.yang.blog.pojo.dto.ChatDto;
import com.yang.blog.pojo.vo.ChatVo;
import com.yang.blog.service.BaseService;

import java.util.List;


/**
 * (Chat)表服务接口
 *
 * @author makejava
 * @since 2023-05-24 15:25:03
 */
public interface ChatService extends BaseService<Chat> {

    /**
     * 聊天
     * @param chatDto
     * @param userId
     * @return
     */
    Result<ChatVo> chat(ChatDto chatDto, Long userId);

    /**
     * 获取聊天消息
     * @param pageQuery
     * @param userId
     * @return
     */
    Result<PageResult<ChatVo>> get(Long toUserId, PageQuery pageQuery, Long userId);

    /**
     * 根据用户id获取用户聊天列表
     * @param userId
     * @return
     */
    Result<List<ChatListVo>> getChatList(Long userId);

    /**
     * 清空未读消息
     * @param userId
     * @param toUserId
     * @return
     */
    Result clearUnread(Long userId, Long toUserId);
}
