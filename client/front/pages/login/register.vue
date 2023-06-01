<template>
  <!-- 登录页面 -->
  <view class="register">
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
        <text>Register</text>
      </view>
      <!--  -->
      <view class="register-box">
        <view class="register-form">
          <view>
            <input type="text" v-model="email" placeholder="请输入邮箱号" />
            <label>
              <u-icon color="#888" name="email-fill" size="25" />
            </label>
          </view>
          <view class="code-view">
            <input type="number" v-model="code" placeholder="请输入验证码" />
            <text class="base-code" v-show="showCode">{{seconds}}</text>
            <button v-show="!showCode" type="button" @click="getEmailCode">获取验证码</button>
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
            <button type="button" @click="register">立即注册</button>
            <view class="other">
              <text @click="goLogin">前往登录</text>
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
import { getEmailCode, register } from '../../api/user';
export default {
  components: {
    DefaultHeader,
  },
  name: "register",
  data() {
    return {
      email: "",
      password: "",
      code: "",
			seconds: 60,
			secondsFun: null,
      show: false,
			showCode: false,
    };
  },
	watch: {
		seconds(val, oldVal){
			if(val === 0){
				// 清除定时器
				clearInterval(this.secondsFun);
				// 显示获取验证码按钮
				this.showCode = false;
				// 重置时间
				this.seconds = 60;
			}
		}
	},
  methods: {
    back() {
      uni.navigateBack();
    },
    async register() {
			// 邮箱验证正则
			let emailReg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			// 验证码验证正则
			let codeReg = /^\d{4}$/;
			// 密码验证：大小写字母、数字、特殊字符
			let passReg = /^\S{6,16}$/;
			if(!emailReg.test(this.email)){
				uni.$u.toast('用户名格式错误');
			} else if(!codeReg.test(this.code)){
				uni.$u.toast('验证码错误');
			} else if(!passReg.test(this.password)){
				uni.$u.toast('密码格式错误');
			} else {
        // 1、整合注册信息
        let data = {
          email: this.email,
          password: this.password,
          code: this.code
        }
        // 2、发起注册请求
        let res = await register(data);
				uni.$u.toast(res.msg);
			}

		},
    goLogin() {
      uni.navigateTo({
        url: "/pages/login/login",
      });
    },
		// 根据邮箱号获取验证码
		async getEmailCode(){
      // 1、校验邮箱号是否合法
      let emailReg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
      if(!emailReg.test(this.email)){
        uni.$u.toast('邮箱号不合法');
        return;
      }
      // 2、发起验证码获取请求
      let res = await getEmailCode(this.email);
			this.showCode = true;
			uni.$u.toast(res.msg);
			this.secondsFun = setInterval(() => {
				this.seconds--;
			}, 1000);
		},
  },
};
</script>

<style lang='scss' scoped>
.register {
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

    .register-box {
      width: 100%;
      display: flex;
      justify-content: center;

      .register-form {
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
            font-size: 14px;
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
        .code-view {
          display: flex;
          align-items: center;

          input {
            padding: 8px 5px 8px 15px;
            width: calc(100% - 130px);
          }
					.base-code {
						display: block;
						width: 80px;
						height: 30px;
						line-height: 30px;
						text-align: center;
            padding: 3px 0;
						margin: 0 15px;
            font-size: 16px;
            box-shadow: 0 0 8px #ececec;
            border: none;
            border-radius: 20px;
            background-color: rgba(192,192,192,.4);
            color: #213b65;
					}
          button {
            width: 80px;
            padding: 3px 0;
            font-size: 12px;
            box-shadow: 0 0 8px #ececec;
            border: none;
            border-radius: 20px;
            background-color: white;
            color: #417ee2;
            &::after {
              border: none;
            }
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
