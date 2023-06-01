<template>
  <view class="user-dynamic-item">
    <!-- 日期 -->
    <view class="date">
      <view class="day">{{ dynamicDetail.day }}</view>
      <view class="month">{{ dynamicDetail.month }}月</view>
    </view>
    <!-- 动态详情 -->
    <view class="dynamic" @longtap="showDel">
      <view class="img" v-if="dynamicDetail.dynamic.imgs.length > 0">
        <image mode="aspectFill" :src="dynamicDetail.dynamic.imgs[0]"></image>
      </view>
      <view class="content" :style="{width: dynamicDetail.dynamic.imgs.length ? '' : '100%'}">
        <view class="text"
          >{{ dynamicDetail.dynamic.content }}</view
        >
        <view class="imgCount" v-if="dynamicDetail.dynamic.imgs.length > 0">共{{dynamicDetail.dynamic.imgs.length}}张</view>
      </view>
    </view>
  </view>
</template>

<script>
import { delById } from '../../api/dynamic';
export default {
  name: "UserDynamicItem",
  props: ["dynamicDetail", "dynamicIndex"],
  data() {
    return {};
  },
  methods: {
    showDel(){
      if(this.$store.state.user.userInfo.id != this.dynamicDetail.dynamic.user.id){
        return;
      }
      uni.showModal({
        title: "提示",
        content: "是否删除？",
        success: async ({confirm}) => {
          if(confirm){
            await delById(this.dynamicDetail.dynamic.id);
            uni.showToast({
              title: "删除成功",
              icon: "none",
              duration: 1000
            })
            // 提示父级删除
            this.$emit("del", this.dynamicIndex);
            // 修改删除标志
            this.$store.commit("REFRESH");
          }
        }
      })
    },
  }
};
</script>

<style lang='scss' scoped>
.user-dynamic-item {
  display: flex;
  margin: 15px 0;

  .date {
    width: 60px;
    .day {
      font-size: 18px;
    }
    .month {
      font-size: 14px;
      color: #b5b5b5;
    }
  }

  .dynamic {
    height: 70px;
    width: calc(100% - 60px);
    display: flex;
    position: relative;
    .img {
      box-sizing: border-box;
      height: 100%;
      image {
        width: 70px;
        height: 70px;
      }
    }

    .content {
      box-sizing: border-box;
      min-width: calc(100% - 70px);
      .text {
        padding: 5px 5px;
        margin-left: 2px;
        min-height: 24px; // 文字超过三行隐藏，显示省略号
        display: -webkit-box;
        overflow: hidden;
        text-overflow: ellipsis;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        background-color: #f6f8f7;
      }
      .imgCount {
        padding: 0px 0 0px 5px;
        font-size: 12px;
        color: #adadad;
      }
    }
  }
}
</style>