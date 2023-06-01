// 聊天仓库
// WebSocket容器
import Vue from 'vue';
import { clearUnread } from '../api/chat';

const dynamic = {
  state: {
    chatList: [], // 聊天列表
    messageList: [], // 消息列表
    nowChatId: "", // 当前聊天id
  },
  mutations: {
    ADD_CHAT_LIST(state, {item, flag}){
      for(const index in state.chatList){
        if(state.chatList[index].user.id == item.user.id){
          // 如果该用户的聊天列表已存在，则添加unread
          if(flag && item.user.id != state.nowChatId){
            item.unread += state.chatList[index].unread + 1;
          }
          state.chatList.splice(index, 1);
          break;
        }
      }
      state.chatList.unshift(item);
    },
    INIT_CHAT_LIST(state, list){
      state.chatList = list;
    },
    DEL_CHAT_ITEM(state, index){
      Vue.prototype.$sqlite.chatList.delByToId(state.chatList[index].toId);
      state.chatList.splice(index, 1);
    },
    ADD_MESSAGE_LIST(state, data){
      if(state.nowChatId == data.toUser.id){
        state.messageList.push(data);
        // 将聊天信息添加到sqlite
        Vue.prototype.$sqlite.message.add(data);
        clearUnread(data.toUser.id); // 清空与该好友的未读消息
      }
    },
    /**
     * 
     * @param {*} state 
     * @param {*} param1 flag: 是否为第一次加载的数据
     */
    GET_CHAT_MESSAGE(state, {data, flag}){
      data = data.reverse();
      if(flag){
        state.messageList = [];
      }
      state.messageList.push(...data);
    },
    CLEAR_UNREAD(state, id){
      for(const index in state.chatList){
        if(state.chatList[index].user.id == id){
          // 如果该用户的聊天列表已存在，则添加unread
          state.chatList[index].unread = 0;
          break;
        }
      }
    },
    SET_CHAT_ID(state, id){
      state.nowChatId = id;
    }
  },
  actions: {
    onMessage({ commit }, data){
      // 改造数据
      let item = {
        toId: data.toUser.id,
        user: data.toUser,
        lastMsg: data.content,
        lastMsgTime: data.createTime,
        unread: 0
      };
      commit("ADD_CHAT_LIST", {item, flag: true});
      commit("ADD_MESSAGE_LIST", data);
    },
  }
};

export default dynamic;