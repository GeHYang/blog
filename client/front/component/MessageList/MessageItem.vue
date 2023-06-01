<template>
  <!-- 消息列表项 -->
  <view class="message-item">
    <u-row>
      <u-col span="2">
        <view class="left">
          <!-- 头像 -->
          <image :src="messageItem.user.avatar"></image>
        </view>
      </u-col>
      <u-col span="8">
        <view class="center">
          <!-- 用户名 -->
          <view class="name">{{ messageItem.user.nickName }}</view>
          <!-- 最后一条消息内容 -->
          <view class="content">{{ messageItem.lastMsg }}</view>
        </view>
      </u-col>
      <u-col span="2">
        <view class="right">
          <!-- 最后一条消息时间 -->
          <view class="time">{{ lastTime }}</view>
          <!-- 未读消息数 -->
          <view class="badge-box">
            <slot></slot>
          </view>
        </view>
      </u-col>
    </u-row>
  </view>
</template>

<script>
export default {
  name: "MessageItem",
  props: ["messageItem"],
  data() {
    return {};
  },
  computed: {
    lastTime(){
      return this.parseDate(new Date(), new Date(this.messageItem.lastMsgTime))
    }
  },
  methods: {
    
  },
};
</script>

<style lang='scss' scoped>
.message-item {
  padding: 0 20px;
  border-bottom: 1px solid #ececec;
  .left,
  .center,
  .right {
    box-sizing: border-box;
    height: 70px;
    display: flex;
    align-items: center;
  }
  .left {
    image {
      width: 50px;
      height: 50px;
      border-radius: 50%;
    }
  }
  .center {
    flex-direction: column;
    align-items: flex-start;
    justify-content: center;
    padding-left: 5px;

    .name,
    .content {
      width: 100%;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .name {
      font-size: 17px;
      font-weight: 500;
      margin-bottom: 5px;
    }
    .content {
      font-size: 12px;
      color: #b5b5b5;
    }
  }
  .right {
    align-items: flex-end;
    justify-content: space-between;
    flex-direction: column;
    .time {
      margin-top: 15px;
      font-size: 12px;
      color: #b5b5b5;
    }
    .badge-box {
      padding-bottom: 15px;
    }
  }
  &:active {
    background-color: #efefef;
  }
}
</style>