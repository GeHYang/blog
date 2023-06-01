<template>
  <!-- 用户动态列表 -->
  <view class="user-dynamic-list">
    <!-- 年份 -->
    <view class="year">{{ dynamicList.year }}</view>
    <!-- 动态信息 -->
    <user-dynamic-item v-for="(item, index) in dynamicList.detail" 
      @click.native="goDynamicDetail(item)" 
      :key="index"
      :dynamicIndex="index"
      :dynamicDetail="item"
      @del="delByIndex"
    />
    
  </view>
</template>

<script>
import UserDynamicItem from './UserDynamicItem.vue';
export default {
  components: { UserDynamicItem },
  name: "UserDynamicList",
  props: ["dynamicList"],
  data() {
    return {};
  },
  methods: {
    // 跳转动态详情页
    goDynamicDetail({dynamic}) {
      uni.navigateTo({
        url: "/pages/dynamicDetail/dynamicDetail",
        // 往动态详情页面传递内容
        success: function (res) {
          // 通过eventChannel向被打开页面传送数据
          res.eventChannel.emit("dynamicDetail", {data: dynamic});
        },
      });
    },
    // 删除
    delByIndex(index){
      this.dynamicList.detail.splice(index, 1);
    }
  }
};
</script>

<style lang='scss' scoped>
.user-dynamic-list{
  padding: 0 20px;

  .year {
    font-size: 22px;
    margin: 10px 0;
  }
}
</style>