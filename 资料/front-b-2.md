# 评论表

一级评论、二级评论、评论内容、评论内容类型、回复的评论、@

## 1、SQL

````sql
create table comment(
	id bigint primary key comment '评论id',
    dynamic_id bigint not null comment '动态id',
   	pid bigint null comment '父评论id',
    content varchar(2000) not null comment '评论内容',
    type int not null default 0 comment '评论类型：0消息',
    reply_id bigint null comment '回复的评论id',
    remind_user_id bigint null comment '@的人的id',
    create_time datetime not null comment '创建时间',
	create_by bigint not null comment '创建者id',
	update_time datetime not null comment '更新时间',
	update_by bigint not null comment '更新者id',
	del_flag int not null default 0 comment '删除状态：0正常，1已删除'
);
````

# git使用

````shell
配置账户
git config --global user.email "邮箱号"
git config --global user.name "gitee名称"

初始化本地仓库
git init

链接远程仓库
git remote add origin https://gitee.com/fengyu118/yiban.git  

查看仓库状态
git status

以下三步必须按顺序执行
将目录下所有文件添加到暂存区
git add .

将暂存区内容添加到本地仓库
git commit -m '备注信息'

将本地仓库文件推送至远程仓库
首次推送需要origin master,之后可以直接使用git push推送
git push -u origin master

把远程仓库的内容拉取到本地
git pull origin master

排除文件
touch .gitignore
````



# 消息队列使用

## 1、启动RabbitMQ



## 2、公共模块引入amqp依赖

````xml
<!--AMQP依赖，包含RabbitMQ-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
````

## 3、改造注册验证码发送功能

**添加配置**

````yaml
  rabbitmq:
    host: 192.168.121.128  # 自己的虚拟机ip地址
    port: 5672 # 端口
    username: admin
    password: admin
    virtual-host: /
````

## 4、测试amqp发送消息

````java
@SpringBootTest
public class UserApplicationTest {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Test
    public void testRabbitMQSendMsg(){
        String queueName = "email.code";
        String msg = "123456";
        rabbitTemplate.convertAndSend(queueName, msg);
    }
}
````



## 5、改造email模块

### 5.1、添加配置

````yaml
  rabbitmq:
    host: 192.168.121.128  # 自己的虚拟机ip地址
    port: 5672 # 端口
    username: admin
    password: admin
    virtual-host: /
````

### 5.2、队列监听

````java
@Component
@Slf4j
public class EmailQueueListener {

    @Resource
    private EmailService emailService;

    @RabbitListener(queues = "email.code")
    public void emailCodeListener(EmailEntity entity){
        log.info("监听到email.code队列到消息");
        System.out.println(entity);
        emailService.sendEmail(entity);
    }
}
````



# 聊天功能

### 生成模板改造

````java
##导入宏定义
$!{define.vm}

##保存文件（宏定义）
#save("/entity", ".java")

##包路径（宏定义）
#setPackageSuffix("entity")

##自动导入包（全局变量）
$!{autoImport.vm}
import com.yang.blog.pojo.entity.BaseEntity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.*;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
##表注释（宏定义）
#tableComment("表实体类")
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("$!{tableInfo.obj.name}")
@ApiModel(description = "${tableInfo.comment}")
public class $!{tableInfo.name} extends BaseEntity implements Serializable {
#foreach($column in $tableInfo.pkColumn)
    #if(${column.comment})//${column.comment}#end
    
    @TableId
    private $!{tool.getClsNameByFullName($column.type)} $!{column.name};
#end

#foreach($column in $tableInfo.otherColumn)
    #if(!$!{column.name.contains('create')} && !$!{column.name.contains('update')} && !$!{column.name.contains('del')})
#if(${column.comment})//${column.comment}#end
	
	@ApiModelProperty(name = "${column.name}", value = "${column.comment}")
    private $!{tool.getClsNameByFullName($column.type)} $!{column.name};
#end
#end

}
````



## 1、引入WebSocket依赖

````xml
<!-- WebSocket -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
````

## 2、配置WebSocket

````java
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
````

## 3、编写WebSocket操作类

````java
package com.yang.blog.chat.handle;

import com.alibaba.fastjson.JSON;
import com.yang.blog.model.Result;
import com.yang.blog.model.StatusEnum;
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


    public void sendToUserById(Long userId, WSResult result) throws IOException {
        WebSocket userWS = map.get(userId);// 拿到对方实例
        userWS.session.getBasicRemote().sendText(JSON.toJSONString(result));

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
````

