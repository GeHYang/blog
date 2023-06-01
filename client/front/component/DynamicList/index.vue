<template>
  <!-- 动态列表 -->
  <view class="dynamic-list">
    <dynamic-item
      @click.native="goDynamicDetail(dynamic)"
      v-for="(dynamic, index) in dynamicList"
      :key="index"
      :dynamicDetail="dynamic"
    />
    <u-loadmore 
        v-show="loadmore"
        :status="pageQuery.end ? 'nomore' : 'loadmore'"
        @loadmore="loadmoreDynamic"
				color="#1CD29B"
				lineColor="#1CD29B"
				line
    />
  </view>
</template>

<script>
import DynamicItem from "./DynamicItem.vue";
import { page } from '../../api/dynamic';

export default {
  name: "DynamicList",
  components: { DynamicItem },
  props: {
    loadmore: {
      type: Boolean,
      default: false
    },
  },
  data() {
    return {
      dynamicList: [],
      pageQuery: {
        current: 1,
        size: 10,
        queryModel: {},
        end: false,
      }
    };
  },
  async created(){
    this.reload();
  },
  mounted(){
    // 监听刷新
    this.$bus.$on("refresh", () => {
      this.reload();
    })
  },
  methods: {
    // 刷新页面
    async reload(){
      let data = await page(this.pageQuery);
      data = data.data.data;
      this.dynamicList = data;
    },
    // 跳转动态详情页
    goDynamicDetail(dynamic) {
      uni.navigateTo({
        url: "/pages/dynamicDetail/dynamicDetail?id=" + dynamic.id,
        // 往动态详情页面传递内容
        success: function (res) {
          // 通过eventChannel向被打开页面传送数据
          res.eventChannel.emit("dynamicDetail", {data: dynamic});
        },
      });
    },
    // 加载更多动态
    loadmoreDynamic(){
      if(this.pageQuery.end) {

        return;
      }
      this.pageQuery.current++;
      page(this.pageQuery).then(({data}) => {
        data = data.data;
        if(data.length < this.pageQuery.size){
          this.pageQuery.end = true;
          this.pageQuery.current--;
        }
        this.dynamicList.push(...data);
      })
      this.$emit("closeLoadmore");
    }
  },
};
</script>

<style lang='scss' scoped>
.dynamic-list {
  box-sizing: border-box;
  padding-bottom: 40px;
}
</style>