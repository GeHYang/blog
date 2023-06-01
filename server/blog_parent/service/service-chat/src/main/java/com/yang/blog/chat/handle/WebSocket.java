package com.yang.blog.chat.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yang.blog.common.JacksonObjectMapper;
import com.yang.blog.model.WSResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Yang
 * @create: 2023-05-24
 * @Description: WebSocket操作类
 */
@ServerEndpoint("/ws/{userId}")
@Component
@Slf4j
public class WebSocket {

    // 单个连接的session
    private Session session;
    private Long userId;

    // 用来存储连接
    private static ConcurrentHashMap<Long, WebSocket> map = new ConcurrentHashMap<>();

    /**
     * 建立WebSocket连接时触发该方法
     * @param userId 连接者id
     * @param session 连接session
     */
    @OnOpen
    public void onOpen(@PathParam("userId") Long userId, Session session){
        this.session = session;
        this.userId = userId;

        if(!Objects.isNull(map.get(userId))){
            // 断开原有连接
            try {
                map.get(userId).session.close();
            } catch (IOException e) {

            }
            map.remove(userId);
        }
        map.put(userId, this);
        log.info(userId + " 连接成功！当前连接数为：" + map.size());
    }


    public void sendToUserById(Long userId, WSResult result) {
        WebSocket userWS = map.get(userId);// 拿到对方实例
        try {
            JacksonObjectMapper objectMapper = new JacksonObjectMapper();

            userWS.session.getBasicRemote().sendText(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("用户不在线");
        }

    }

    /**
     * 监听数据接收，只能接收字符串
     * @param message
     */
    @OnMessage
    public void onMessage(String message){

    }



    /**
     * 发生错误
     *
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable) {
        log.error(this.userId + " 发生错误");
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        try {
            this.session.close();
        } catch (IOException e) {

        }
        map.remove(this.userId); // 清空
        log.info(this.userId + " 断开连接, 当前连接数为：" + map.size());
    }

}
