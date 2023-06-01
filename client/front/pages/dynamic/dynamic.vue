<template>
  <view class="dynamic">
    <my-header />
    <view class="main">
      <dynamic-list :loadmore="loadmore" @closeLoadmore="loadmore = false" />
      <view class="publish">
        <u-icon
          @click="toPublish"
          name="plus-circle-fill"
          color="#1989fa"
          size="50"
        ></u-icon>
      </view>
    </view>
    <my-footer :name="'dynamic'" />
  </view>
</template>

<script>
import MyHeader from "../../component/MyHeader";
import DynamicList from "../../component/DynamicList";

export default {
  components: { MyHeader, DynamicList },
  data() {
    return {
      loadmore: false,
    };
  },
  onLoad(){
    this.$bus.$on("publish", () => {
      uni.startPullDownRefresh();
    })
  },
  onShow(){
    if(this.$store.state.dynamic.refreshFlag){
      uni.startPullDownRefresh();
      // 重置刷新标志
      this.$store.commit("RESET_REFRESH");
    }
  },
  onPullDownRefresh(e){
    this.$bus.$emit("refresh");
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000);
  },
  onReachBottom(){
    this.loadmore = true;
  },
  methods: {
    // 跳转到发布动态页面
    toPublish() {
      uni.navigateTo({
        url: "/pages/publishDynamic/publishDynamic",
      });
    },
  },
};
</script>

<style lang="scss">
.dynamic {
  .main {
    overflow: initial;
    height: initial;
    padding-bottom: 20px;
    .publish {
      position: fixed;
      bottom: 70px;
      right: 20px;

      &::after {
        content: "";
        z-index: -1;
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        border-radius: 50%;
        width: 40px;
        height: 40px;
        background-color: white;
      }
    }
  }
}
</style>
