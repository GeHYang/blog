<template>
<!-- 推荐列表 -->
  <view class="recommend-list">
    <view class="column">
      <recommend-item v-for="(recommend, index) in recommendLeft" :key="index" :recommend="recommend" />
    </view>
    <view class="column">
      <recommend-item v-for="(recommend, index) in recommendRight" :key="index" :recommend="recommend" />
    </view>
  </view>
</template>

<script>
import RecommendItem from './RecommendItem.vue';
import {recommend} from "../../mock/recommend";

export default {
  components: { RecommendItem },
  name: "",
  data() {
    return {
      recommendLeft: [],
      recommendRight: [],
    };
  },
  created() {
    let leftHeight = 0;
    let rightHeight = 0;
    recommend.forEach(val => {
      if(leftHeight <= rightHeight){
        this.recommendLeft.push(val);
        leftHeight += val.height;
      } else {
        this.recommendRight.push(val);
        rightHeight += val.height;
      }
    })
  }
};
</script>

<style lang='scss' scoped>
.recommend-list {
  width: 100%;
  display: flex;
  justify-content: space-between;
  padding-bottom: 20px;
  .column{
    width: 48%;
  }
}
</style>