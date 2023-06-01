<template>
  <view>
    <notify-msg
      v-for="(notify, index) in notifyList"
      :key="index"
      :tips="tipsList[current]"
      :notify="notify"
    ></notify-msg>
  </view>
</template>

<script>
import NotifyMsg from "./NotifyMessage.vue";
import { notifyList } from "../../mock/notify";
export default {
  name: "NotifyMsgList",
  components: { NotifyMsg },
  props: ["current"],
  data() {
    return {
      tipsList: ["赞了我的评论", "对我的动态发表了评论", "@了我", "关注了我"],
      notifyList: notifyList[this.current],
    };
  },
};
</script>

<style lang='scss' scoped>
</style>