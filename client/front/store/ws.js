// WebSocket容器
import ws from '../utils/ws';
import store from './index';

const dynamic = {
  state: {
    chatMsg: {}, // 接收到的数据
  },
  mutations: {
    CREATE_WS(state){
      ws(store.state.user.userInfo.id, ({data}) => {
        data = JSON.parse(data);
        if(data.type === "chat"){
          store.dispatch("onMessage", data.data);
        }
      });
    }
  },
  actions: {
  }
};

export default dynamic;