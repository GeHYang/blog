<template>
  <view class="account">
    <view class="status_bar">
      <!-- 这里是状态栏 -->
    </view>
    <!-- <my-header /> -->
    <view class="main" v-if="userInfo">
      <!-- 顶部信息 -->
      <view class="top-info">
        <!-- 头像、用户名、ID -->
        <view class="info">
          <!-- 头像 -->
          <view class="avatar">
            <image :src="userInfo.avatar"></image>
          </view>
          <!-- 用户名 -->
          <view class="name">
            <view class="username">{{ userInfo.nickName }}</view>
            <view class="userId">ID：{{ userInfo.id }}</view>
          </view>
          <!-- 性别 -->
          <view class="sex"
            ><u-icon
              :color="userInfo.sex ? 'blue' : 'rgb(234, 33, 100)'"
              name="account-fill"
            ></u-icon
          ></view>
        </view>
        <!-- 数量 -->
        <view class="count" v-show="!showLoginTips">
          <view>
            <view class="num">2378</view>
            <text>发表</text>
          </view>
          <view>
            <view class="num">0</view>
            <text>关注</text>
          </view>
          <view>
            <view class="num">0</view>
            <text>粉丝</text>
          </view>
        </view>
      </view>
      <!-- 个人信息 -->
      <view class="user-info">
        <u-cell-group  v-show="!showLoginTips">
          <u-cell title="用户名" :value="userInfo.username"></u-cell>
          <u-cell title="昵称" :value="userInfo.nickName"></u-cell>
          <u-cell title="手机号" :value="userInfo.phoneNumber"></u-cell>
          <u-cell title="邮箱" :value="userInfo.email"></u-cell>
          <u-cell
            title="我的动态"
            :isLink="true"
            arrow-direction="right"
            @click="toUserDetail"
          ></u-cell>
          <u-cell title="设置" isLink url="/pages/userEdit/userEdit">
            <u-icon slot="value" name="setting-fill">99</u-icon>
          </u-cell>
        </u-cell-group>
      </view>
      <u-modal
        :title="'提示'"
        :show="showLoginTips"
        showCancelButton
        @confirm="goLogin(true)"
        @cancel="goLogin"
      ><text>请登录</text></u-modal>
    </view>
    <my-footer :name="'account'" />
  </view>
</template>

<script>
import MyHeader from "../../component/MyHeader";
import { mapState } from "vuex";
export default {
  components: { MyHeader },
  data() {
    return {
      showLoginTips: false,
    };
  },
  computed: {
    // 使用对象展开运算符将 getter 混入 computed 对象中
    ...mapState({
      userInfo: (state) => state.user.userInfo,
    }),
  },
  onShow(){
    // 登录校验
    if(JSON.stringify(this.userInfo) == '{}' || !this.userInfo){
      this.showLoginTips = true;
    } else {
      this.showLoginTips = false;
    }
  },
  methods: {
    goLogin(flag){
      if(flag) {
        // 前往登录页面
        uni.navigateTo({url: '/pages/login/login'})
      } else {
        uni.switchTab({url: '/pages/index/index'})
      }
    },
    toUserDetail(){
      uni.navigateTo({url: '/pages/userDetail/userDetail'})
    }
  }
};
</script>

<style lang="scss">
.account {
  ::v-deep .u-cell__value{
    width: 80%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .status_bar {
    height: var(--status-bar-height);
    width: 100%;
  }
  .main {
    height: calc(100vh - 51px);
    .top-info {
      box-sizing: border-box;
      overflow: hidden;
      width: 100%;
      height: 240px;
      background-color: #417ee2;
      padding: 0 20px;

      .info {
        width: 100%;
        box-sizing: border-box;
        display: flex;
        align-items: center;
        padding: 30px 0;
        .avatar {
          overflow: hidden;
          width: 80px;
          height: 80px;
          display: flex;
          align-items: center;

          image {
            width: 70px;
            height: 70px;
            border: 3px groove;
            border-radius: 50%;
          }
        }

        .name {
          box-sizing: border-box;
          width: calc(100% - 80px - 40px);
          padding: 0 10px;
          color: white;

          .username,
          .userId {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          .username {
            font-size: 18px;
            line-height: 35px;
          }
          .userId {
            font-size: 12px;
          }
        }
      }

      .count {
        display: flex;
        justify-content: space-around;
        color: white;
        text-align: center;

        view {
          .num {
            font-weight: 600;
            line-height: 20px;
          }

          text {
            font-size: 12px;
          }
        }
      }
    }

    .user-info {
      overflow: hidden;
      box-sizing: border-box;
      transform: translateY(-40px);
      padding: 30px 20px;
      height: calc(100% - 240px + 40px);
      background-color: #fff;
      // border-radius: 左上 右上 左下 右下;
      border-radius: 50px 50px 0 0;
    }
  }
}
</style>
