<template>
  <!-- 推荐列表项 -->
  <view class="recommend-item">
    <view class="top">
      <image @click="showBigImg = true" mode="widthFix" :src="recommend.cover" />
      <u-overlay :opacity="0.8" :duration="100" :show="showBigImg" @click="showBigImg = false">
        <view class="main big-image">
          <image mode="widthFix" :src="recommend.cover" />
        </view>
      </u-overlay>
    </view>
    <view class="center"> {{ recommend.title }} </view>
    
    <view class="bottom">
      {{recommend.content}}
    </view>
  </view>
</template>

<script>

export default {
  name: "",
  props: ["recommend"],
  data() {
    return {
      showBigImg: false,
    };
  },
};
</script>

<style lang='scss' scoped>
.recommend-item {
  box-sizing: border-box;
  margin-bottom: 10px;
  width: 100%;
  padding: 5px;
  border-radius: 5px;
  box-shadow: 0 0 10px #cfcfcf;
  overflow: hidden;

  .top {
    width: 100%;
    border-radius: 5px;
    overflow: hidden;
    image {
      width: 100%;
    }
  }

  .center {
    line-height: 35px;
    font-size: 18px;
    font-weight: 600;
  }

  .bottom {
    max-height: 54px;
    // 文字超过三行隐藏，显示省略号
    display: -webkit-box;
    overflow: hidden;
    text-overflow: ellipsis;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;

    font-size: 13px;
    color: #d3d3d3;
  }
}
</style>