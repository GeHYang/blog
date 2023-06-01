// 动态store
const dynamic = {
  state: {
    refreshFlag: false,// 刷新动态页面标志
  },
  mutations: {
    REFRESH(state){
      state.refreshFlag = true;
    },
    RESET_REFRESH(state){ 
      state.refreshFlag = false;
    }
  },
  actions: {
  }
};

export default dynamic;