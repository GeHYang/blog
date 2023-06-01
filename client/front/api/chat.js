// 聊天接口集合
import http from '../utils/request';

/**
 * 发送聊天信息
 * @param {*} chatDto 
 * @returns 
 */
export const chat = (chatDto) => {
  return http({
    url: "/chat",
    data: chatDto
  })
}
/**
 * 获取聊天记录
 * @param {*} pageQuery 
 * @param {*} toUserId 
 * @returns 
 */
export const get = (pageQuery, toUserId) => {
  return http({
    url: "/chat/" + toUserId,
    method: 'get'
  })
}
/**
 * 获取聊天列表
 * @returns 
 */
export const getChatList = () => {
  return http({
    url: "/chat/chatList",
    method: "get"
  });
}
/**
 * 清空未读消息
 * @param {*} toUserId 
 * @returns 
 */
export const clearUnread = (toUserId) => {
  return http({
    url: "/chat/chatList/" + toUserId,
    method: "delete"
  })
}
