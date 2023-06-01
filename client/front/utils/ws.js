
// 封装WebSocket
export default function ws(userId, callback){
  // 1、创建WebSocket连接
  uni.connectSocket({
    url: "ws://192.168.0.4:32010/ws/" + userId,
    success(){
      uni.onSocketMessage(callback);
    },
    fail(){
      console.log("出错啦");
    }
  });
}
