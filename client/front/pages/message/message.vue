<template>
  <view class="message">
    <my-header />
    <view class="main">
      <view class="tags">
        <view class="tags-item" @click="toNotify(0)">
          <view><u-icon name="heart-fill" color="#fff" size="30"></u-icon></view>
          <text>收到的赞</text>
        </view>
        <view class="tags-item" @click="toNotify(1)">
          <view><u-icon name="chat-fill" color="#fff" size="30"></u-icon></view>
          <text>评论与回复</text>
        </view>
        <view class="tags-item" @click="toNotify(2)">
          <view><text>@</text></view>
          <text>@我的</text>
        </view>
      </view>
      <view class="message-box">
        <message-list />
      </view>
    </view>
    <my-footer :name="'message'" />
  </view>
</template>

<script>
import MyHeader from "../../component/MyHeader";
import MessageList from "../../component/MessageList"

export default {
  name: "message",
  components: { MyHeader, MessageList },
  data() {
    return {};
  },
  created(){
    // 删除表
    // this.$sqlite.chatList.drop();
    // 创建表
    this.$sqlite.chatList.create();
  },
  mounted(){
    this.$bus.$emit("onShow");
  },
  // onShow(){
  //   this.$bus.$emit("onShow");
  // },
  onHide(){
    this.$bus.$emit("onHide");
  },
  methods: {
    toNotify(index){
      uni.navigateTo({
        url: `/pages/notify/notify?current=${index}`,
      });
    }
  }
};
</script>

<style lang='scss' scoped>
.message {
  .main {
    .tags {
      padding: 15px 20px;
      display: flex;
      justify-content: space-between;
      border-bottom: 1px solid #ececec;
      .tags-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        view {
          display: flex;
          justify-content: center;
          align-items: center;
          width: 50px;
          height: 50px;
          border-radius: 50%;

          text{
            color: white;
            font-size: 25px;
            transform: translateY(-5%);
          }
        }
        >text{
          display: block;
          margin-top: 10px;
          font-size: 14px;
        }
        &:nth-child(1) view{
          background-color: #fe4f51;
        }
        &:nth-child(2) view{
          background-color: #3071f5;
        }
        &:nth-child(3) view{
          background-color: #13ce5d;
        }
      }
    }

    .message-box {
      overflow: hidden;
      overflow-y: scroll;
      height: calc(100% - 110px);
    }
  }
}
</style>