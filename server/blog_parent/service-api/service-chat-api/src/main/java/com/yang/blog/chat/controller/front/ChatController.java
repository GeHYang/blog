package com.yang.blog.chat.controller.front;

import com.yang.blog.chat.entity.vo.ChatListVo;
import com.yang.blog.chat.service.ChatService;
import com.yang.blog.model.PageQuery;
import com.yang.blog.model.PageResult;
import com.yang.blog.model.Result;
import com.yang.blog.pojo.dto.ChatDto;
import com.yang.blog.pojo.vo.ChatVo;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Yang
 * @create: 2023-05-24
 * @Description:
 */
@RestController
@RequestMapping("/front/chat")
public class ChatController {
    @Resource
    private ChatService chatService;

    @PostMapping
    public Result<ChatVo> chat(@RequestBody ChatDto chatDto, @RequestHeader("user-id") Long userId){
        return chatService.chat(chatDto, userId);
    }
    // 获取聊天信息
    @GetMapping("/{toUserId}")
    public Result<PageResult<ChatVo>> get(@PathVariable("toUserId") Long toUserId, @RequestBody(required = false) PageQuery pageQuery, @RequestHeader("user-id") Long userId){
        return chatService.get(toUserId, pageQuery, userId);
    }
    // 获取聊天列表信息
    @GetMapping("/chatList")
    public Result<List<ChatListVo>> getChatList(@ApiIgnore @RequestHeader("user-id") Long userId){
        return chatService.getChatList(userId);
    }

    // 清空未读消息
    @DeleteMapping("/chatList/{toUserId}")
    public Result clearUnread(@ApiIgnore @RequestHeader("user-id") Long userId, @PathVariable("toUserId") Long toUserId){
        return chatService.clearUnread(userId, toUserId);
    }

}
