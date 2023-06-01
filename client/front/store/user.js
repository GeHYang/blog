// 引入缓存操作
import storage from '../utils/storage';

const user = {
  state: {
    userInfo: {},
    token: "",
  },
  mutations: {
    // 退出登录
    LOGOUT(state) {
      state.userInfo = null;
      // 清空本地缓存
      storage.del("userInfo");
      storage.del("token");
      // 清除vuex的token
      state.token = "";
      // 跳转登录
      uni.reLaunch({
        url: `/pages/index/index`
      })
    },
    LOGIN(state, {userInfo, token}){
      state.userInfo = userInfo;
      state.token = token;
      // 保存用户信息到本地
      storage.set("userInfo", userInfo, true);
      storage.set("token", token);
    },
    // 加载用户信息
    LOAD_USERINFO(state){
      // 从localstorege中拿到用户数据
      let userInfo = storage.get("userInfo", true);
      let token = storage.get("token");
      state.userInfo = userInfo;
      state.token = token;
    },
    // 更新用户信息
    UPDATE_INFO(state, userInfo){
      state.userInfo = userInfo;
      // 修改本地数据
      storage.set("userInfo", userInfo, true)
    }
  },
  actions: {
    // 退出登录
    logout(context){
      context.commit("LOGOUT");// 提交退出登录操作
    },
    login(context, data){
      context.commit("LOGIN", data);
    }
  }
};

export default user;