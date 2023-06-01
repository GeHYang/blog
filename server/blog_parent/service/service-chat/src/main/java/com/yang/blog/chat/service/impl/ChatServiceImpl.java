package com.yang.blog.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.blog.chat.entity.pojo.Chat;
import com.yang.blog.chat.entity.vo.ChatListVo;
import com.yang.blog.chat.handle.WebSocket;
import com.yang.blog.chat.mapper.ChatMapper;
import com.yang.blog.chat.service.ChatService;
import com.yang.blog.model.*;
import com.yang.blog.pojo.dto.ChatDto;
import com.yang.blog.pojo.vo.ChatVo;
import com.yang.blog.utils.BeanCopyUtils;
import com.yang.blog.utils.RedisCache;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * (Chat)表服务实现类
 *
 * @author makejava
 * @since 2023-05-24 15:25:03
 */
@Service("chatService")
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {

    @Resource
    private WebSocket webSocket;
    @Resource
    private RedisCache redisCache;

    @Override
    public Result<ChatVo> chat(ChatDto chatDto, Long userId) {
        // 1、将dto转换为数据库实体
        Chat chat = BeanCopyUtils.convert(chatDto, Chat.class);
        // 2、设置发送人
        chat.setCreateBy(userId);
        // 3、构建id
        chat.setId(IdWorker.getId());
        chat.setCreateTime(new Date());
        // 4、发送给对方
        ChatVo chatVo = BeanCopyUtils.convert(chat, ChatVo.class);
        try {
            chatVo.setIsMe(false);
            // 添加用户信息
            chatVo.setToUser(getUserVoById(userId));
            // 封装VO
            webSocket.sendToUserById(chat.getRecvId(), new WSResult<>(WSResult.Types.chat, chatVo));
        } catch (Exception e) {

        }
        // 5、保存数据库
        save(chat);
        // 6、将未读消息的id存到redis中
        HashOperations opsForHash = redisCache.redisTemplate.opsForHash();
        List<Long> chatIds = (List<Long>) opsForHash.get(chat.getRecvId() + "", userId + "");
        if (Objects.isNull(chatIds)){
            chatIds = new ArrayList<>();
        }
        chatIds.add(chat.getId());
        opsForHash.put(chat.getRecvId() + "", userId + "", chatIds);

        chatVo.setToUser(getUserVoById(chat.getRecvId()));
        chatVo.setIsMe(true);

        return Result.ok(StatusEnum.REQUEST_SUCCESS, chatVo);
    }

    @Override
    public Result<PageResult<ChatVo>> get(Long toUserId,PageQuery pageQuery, Long userId) {
        if (Objects.isNull(pageQuery)){
            pageQuery = new PageQuery();
            pageQuery.setQueryModel(new QueryModel());
        }
        // 1、从redis中拿到toUserId发来的未读消息id集合
        List<Long> chatIds = (List<Long>) redisCache.redisTemplate.opsForHash().get(userId + "", toUserId + "");
        // 2、根据id集合查询所有聊天信息
        Page<Chat> page = new Page<>(pageQuery.getCurrent(), pageQuery.getSize());
        LambdaQueryWrapper<Chat> queryWrapper = new LambdaQueryWrapper<>();
        if(!Objects.isNull(chatIds)){
            queryWrapper.in(Chat::getId, chatIds).orderByDesc(Chat::getCreateTime);
            page(page, queryWrapper);
        }
        // 3、清空redis对应信息id
        redisCache.redisTemplate.opsForHash().delete(userId + "", toUserId + "");
        // 封装vo
        List<Chat> list = page.getRecords();

        List<ChatVo> chatVos = new ArrayList<>();
        for (Chat chat : list) {
            // 封装Vo
            ChatVo chatVo = BeanCopyUtils.convert(chat, ChatVo.class);
            // 是否为本人发送
            chatVo.setIsMe(chat.getCreateBy().equals(userId));
            // 封装对方用户信息
            if(chatVo.getIsMe()){
                chatVo.setToUser(getUserVoById(chat.getRecvId()));
            } else {
                chatVo.setToUser(getUserVoById(chat.getCreateBy()));
            }
            chatVos.add(chatVo);
        }

        PageResult<ChatVo> pageResult = new PageResult<>(page, chatVos);
        return Result.ok(StatusEnum.REQUEST_SUCCESS, pageResult);
    }

    @Override
    public Result<List<ChatListVo>> getChatList(Long userId) {
        // 1、从redis中获取该用户的所有未读消息id
        Map chatIdMap = redisCache.redisTemplate.opsForHash().entries(userId + "");
        List<Long> chatIds = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        chatIdMap.forEach((key, val) -> {
            List<Long> ids = (List<Long>) val;
            Long lastMsgId = ids.get(ids.size() - 1);
            chatIds.add(lastMsgId);
            map.put((String) key, ids.size());
        });
        // 2、根据id查询聊天信息
        LambdaQueryWrapper<Chat> queryWrapper = new LambdaQueryWrapper<>();
        List<Chat> chatList = new ArrayList<>();
        if(!Objects.isNull(chatIds) && chatIds.size() > 0){
            queryWrapper.in(Chat::getId, chatIds);
            chatList = list(queryWrapper);
        }
        // 3、封装到chatListVo
        List<ChatListVo> chatListVos = new ArrayList<>();
        chatList.forEach(val -> {
            ChatListVo chatListVo = new ChatListVo(val.getCreateBy(), getUserVoById(val.getCreateBy()),
                    val.getContent(), val.getCreateTime(), map.get(val.getCreateBy() + ""));
            chatListVos.add(chatListVo);
        });
        return Result.ok(StatusEnum.REQUEST_SUCCESS, chatListVos);
    }

    @Override
    public Result clearUnread(Long userId, Long toUserId) {
        redisCache.redisTemplate.opsForHash().delete(userId + "", toUserId + "");
        return Result.ok(StatusEnum.REQUEST_SUCCESS);
    }
}

