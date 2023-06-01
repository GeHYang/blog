<template>
  <!-- 非底部导航栏页面头部导航栏 -->
  <view>
    <u-navbar
      :border="true"
      :leftText="'返回'"
      :placeholder="true"
      :title="navTitle"
      @leftClick="toPage"
    >
      <view slot="right">
        <slot></slot>
      </view>
    </u-navbar>
  </view>
</template>

<script>
export default {
  name: "DefaultHeader",
  props: {
    "navTitle": {
      type: String,
      default: ""
    },
    option: {
      type: Object,
      default: () => {
        return {url: "", tabbar: false}
      }
    }
  },
  data() {
    return {};
  },
  methods: {
    /**
     * url：跳转到路径
     * tabbar：是否是有底部导航栏的页面
     */
    toPage(){
      const option = this.option;
      if(option.url){
        if(option.tabbar){
          uni.switchTab({
            url: option.url
          })
        } else {
          uni.navigateTo({
            url: option.url
          })
        }
      } else {
        uni.navigateBack();
      }
    }
  }
};
</script>

<style lang='scss' scoped>

</style>