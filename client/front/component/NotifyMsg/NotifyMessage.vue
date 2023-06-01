<template>
  <view class="notify-msg">
    <!-- 头像 -->
    <view class="avatar">
      <image :src="notify.user.avatar"></image>
    </view>
    <!-- 信息 -->
    <view class="content">
      <view class="msg"
        ><text class="nickName">{{notify.user.nickName}}</text
        ><text class="tips">{{ tips }}</text></view
      >
      <!-- 回复信息 -->
      <view class="recvMsg" v-if="notify.comment">{{ notify.comment.content }}</view>
      <view class="time-box">
        <view class="time">{{ notify.createTime }}</view>
        <slot></slot>
      </view>
    </view>
    <!-- 引用 -->
    <view class="quote">
      <view class="dynamic" v-if="notify.dynamic">
        <image v-if="notify.dynamic.imgs.length > 0" :src="notify.dynamic.imgs[0]"></image>
        <view v-else class="no-img">{{ notify.dynamic.content }}</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: "NotifyMsg",
  props: ["tips", "notify"],
  data() {
    return {};
  },
};
</script>

<style lang='scss' scoped>
.notify-msg {
  display: flex;
  justify-content: space-between;
  padding: 5px 20px;
  border-bottom: 1px solid #ececec;
  &:active {
    background-color: #efefef;
  }

  .avatar {
    box-sizing: border-box;
    width: 50px;
    image {
      margin: 2.5px;
      width: 45px;
      height: 45px;
      border-radius: 50%;
    }
  }

  .quote,
  .dynamic{
    box-sizing: border-box;
    width: 50px;
    height: 50px;
  }

  .content {
    box-sizing: border-box;
    width: calc(100% - 100px);
    padding: 0 10px;
    font-size: 14px;
    display: flex;
    flex-direction: column;
    justify-content: center;

    .msg {
      .nickName {
        margin-right: 5px;
        color: #9fbfff;
      }
      .tips {
        color: #b5b5b5;
      }
    }
    .recvMsg {
      font-size: 14px;
    }
    .time-box {
      display: flex;
      .time {
        margin-top: 3px;
        font-size: 12px;
        color: #b5b5b5;
      }
    }
  }

  .quote {
    image {
      width: 45px;
      height: 45px;
      margin: 2.5px;
    }
    .no-img {
      box-sizing: border-box;
      width: 100%;
      height: 100%;
      overflow: hidden;
      font-size: 12px;
      color: #b5b5b5;
      padding: 1px;
      box-shadow: 0 0 2px #ccc;
    }
  }
}
</style>