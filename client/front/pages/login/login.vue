<template>
  <!-- 登录页面 -->
  <view class="login">
    <view class="status_bar">
      <!-- 这里是状态栏 -->
    </view>
    <view class="main _main">
      <i>
        <u-icon @click="back" name="arrow-left" color="#fff" size="25"></u-icon>
      </i>
      <!-- 顶部 -->
      <view class="top">
        <view class="top-icon">
          <u-icon name="account" color="white" size="100" />
        </view>
        <text>LOGIN</text>
      </view>
      <!--  -->
      <view class="login-box">
        <view class="login-form">
          <view>
            <input type="text" v-model="username" placeholder="用户名/邮箱/手机号" />
            <label>
              <u-icon color="#888" name="account-fill" size="25" />
            </label>
          </view>
          <view>
            <input
              type="password"
              v-model="password"
              placeholder="请输入密码"
            />
            <label>
              <u-icon color="#888" name="lock-fill" size="25" />
            </label>
          </view>
          <view class="btn">
            <button type="button" @click="login">立即登录</button>
            <view class="other">
              <text @click="goRegister">注册</text>
              <text>忘记密码</text>
            </view>
          </view>
        </view>
      </view>
      <u-notify ref="uNotify"></u-notify>
    </view>
  </view>
</template>

<script>
import DefaultHeader from "../../component/MyHeader/DefaultHeader.vue";
import {login} from '../../api/user';
export default {
  components: {
    DefaultHeader,
  },
  name: "login",
  data() {
    return {
      username: "3235997717@qq.com",
      password: "123456",
      show: false,
    };
  },
  methods: {
    back() {
      uni.navigateBack();
    },
    login() {
      // 邮箱正则
      let emailReg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
      // 手机号正则
      let phoneNumberReg = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/;
      if(emailReg.test(this.username)){
        // 邮箱登录
        this.Login("email");
      } else if(phoneNumberReg.test(this.username)){
        // TODO 手机号登录
      } else {
        // 用户名登录
        this.Login("username");
      }
    },
    /**邮箱登录*/
    Login(type){
      // 判断密码长度是否足够
      if(this.password.length < 6) {
        uni.$u.toast('密码格式错误');
        return;
      }
      // 构建登录数据
      let data = {
        "password": this.password,
      };
      data[type] = this.username
      // 发起登录请求
      login(data).then((res) => {
        // 判断是否成功
        if(!res.flag){
          uni.$u.toast('用户名或密码错误！');
          return;
        }
        // 提交登录信息到vuex
        this.$store.dispatch("login", {userInfo: res.data, token: res.msg});
        uni.$u.toast('登录成功');
        setTimeout(() => {
          uni.reLaunch({
            url: "/pages/index/index"
          })
        }, 500);

      }).catch(err => {
        console.log(err);
        // 请求失败
        uni.$u.toast(err);
      })
    },
		goRegister(){
			uni.navigateTo({
				url: "/pages/login/register"
			})
		}
	},
};
</script>

<style lang='scss' scoped>
.login {
  .status_bar {
    height: var(--status-bar-height);
    width: 100%;
  }

  .main {
    position: relative;

    i {
      position: absolute;
      z-index: 1;
      top: 10px;
      left: 0;
      font-style: normal;
    }

    .top {
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
      position: relative;
      left: 50%;
      top: -8%;
      width: 150%;
      height: 40%;
      transform: translateX(-50%);
      border-radius: 50%;
      background-color: #417ee2;

      .top-icon {
        border-radius: 50%;
        border: 4px solid #fff;
        margin-bottom: 10px;
      }

      text {
        color: white;
        font-size: 20px;
        font-family: "华文仿宋";
      }

      .top-icon,
      text {
        transform: translateY(30px);
      }
    }

    .login-box {
      width: 100%;
      display: flex;
      justify-content: center;

      .login-form {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;

        view {
          width: 70%;
          position: relative;

          input {
            width: 70%;
            padding: 8px 15px 8px 50px;
            margin: 10px 0;
            box-shadow: 0 0 8px #ececec;
            font-size: 16px;
            border-radius: 20px;
          }

          label {
            position: absolute;
            top: 50%;
            left: 10px;
            padding: 0 3px;
            transform: translateY(-50%);
            border-right: 1px solid #ccc;
          }
        }

        .btn {
          display: flex;
          flex-direction: column;
          align-items: center;
          width: 100%;
          margin-top: 40px;

          button {
            width: 70%;
            border-radius: 30px;
            background-color: #417ee2;
            color: white;

            &:active {
              background-color: #5b9afe;
            }
          }

          .other {
            margin-top: 10px;
            display: flex;
            justify-content: space-between;
            font-size: 12px;
            color: #ccc;
          }
        }
      }
    }
  }
}
</style>
