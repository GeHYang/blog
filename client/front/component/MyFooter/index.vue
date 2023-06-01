<template>
  <view class="my-footer">
    <u-tabbar
      :value="value1"
      @change="changeTab"
      :fixed="true"
      :placeholder="false"
      :safeAreaInsetBottom="false"
    >
      <u-tabbar-item text="首页" icon="home" name="index"></u-tabbar-item>
      <u-tabbar-item text="消息" icon="chat" :dot="showDot" name="message"></u-tabbar-item>
      <u-tabbar-item text="动态" icon="star" name="dynamic"></u-tabbar-item>
      <u-tabbar-item text="我的" icon="account" name="account"></u-tabbar-item>
    </u-tabbar>
  </view>
</template>

<script>
export default {
  name: "MyFooter",
  props: ["name"],
  data() {
    return {
      value1: this.name,
      showDot: true,
    };
  },
  methods: {
    changeTab(name){
      uni.switchTab({
        url: `/pages/${name}/${name}`
      })
    }
  },
};
</script>

<style lang='scss' scoped>
.my-footer {
  width: 100%;
}
</style>