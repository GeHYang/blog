import store from '../store/index';
/**
 * 路由拦截
 */
const intercept = function () {
  let funName = ['navigateTo', 'redirectTo', 'switchTab'];
  const funs = [];
  // 1、将对应的方法拷贝下来
  funName.forEach(val => {
    funs[val] = uni[val];
  })
  // 不需要登录就放行的路径
  function noCheck(path) {
    // 不需要校验的路径集合
    const noCheckPath = [
      "/pages/login/login",
      "/pages/login/register",
      "/pages/index/index",
      "/pages/dynamic/dynamic",
    ];
    for (const p of noCheckPath) {
      if (p == path) return true;
    }
    return false;
  }

  // 2、对uni中存在的funName对应的方法进行获取
  funName.forEach((val) => {
    // 3、对对应的方法进行重写
    uni[val] = function (options = {}) {
      // 4、判断用户是否登录
      if (JSON.stringify(store.state.user.userInfo) != '{}' && store.state.user.userInfo) {
        // 已登录，正常跳转
        funs[val](options);
      } else if (noCheck(options.url)) {
        // 不需要校验登录，正常跳转
        funs[val](options);
      } else {
        uni.showModal({
          title: '警告',
          content: '该功能需要登录才能使用，是否前往登录？？？',
          success: function (res) {
            if (res.confirm) {
              // 未登录，跳转登录页面
              funs.navigateTo({ url: "/pages/login/login" })
            } else if (res.cancel) {// 点击取消，跳转首页
              funs.switchTab({ url: "/pages/index/index" })
            }
          }
        });

      }
    }
  })
}

intercept();