<!-- 自定义头部 -->
<template>
  <view class="my-header">
    <view class="status_bar">
      <!-- 这里是状态栏 -->
    </view>
    <view class="box">
      <view class="left">
        <u-icon
          v-if="!$store.state.user.userInfo"
          @click="goPage('login')"
          name="error-circle"
          color="rgb(251, 73, 73)"
          size="25"
        ></u-icon>
        <u-avatar
          v-else
          :src="$store.state.user.userInfo.avatar"
          @click="goPage('account')"
          size="25"
        ></u-avatar>
      </view>
      <view class="center">
        <input type="text" placeholder="请输入内容" />
      </view>
      <view class="right">
        <u-icon name="bell" @click="toNotify" color="#000" size="23" />
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: "MyHeader",
  data() {
    return {};
  },
  methods: {
    goPage(page) {
      if (page === "login") {
        uni.navigateTo({
          url: `/pages/${page}/${page}`,
        });
      } else {
        uni.switchTab({
          url: `/pages/${page}/${page}`,
        });
      }
    },
    toNotify() {
      uni.navigateTo({
        url: `/pages/notify/notify`,
      });
    },
  },
};
</script>

<style lang='scss' scoped>
.my-header {
  .status_bar {
    height: calc(var(--status-bar-height) + 40px);
    /* #ifdef H5 */
    height: 40px;
    /* #endif */
    width: 100%;
  }

  .box {
    z-index: 100;
    box-sizing: border-box;
    position: fixed;
    top: 0;
    padding: 0 20px;
    padding-top: 40px;
    height: calc(var(--status-bar-height) + 40px);
    /* #ifdef H5 */
    height: 40px;
    padding-top: 0px;
    /* #endif */
    left: 0;
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: white;
    box-shadow: 0 0 5px #ececec;

    .left,
    .right {
      width: 30px;
      text-align: center;
    }

    .center {
      input {
        box-sizing: border-box;
        height: 30px;
        padding: 5px 15px;
        box-shadow: 0 0 2px #ececec;
        border-radius: 15px;
        font-size: 14px;
      }
    }
  }
}
</style>
