<template>
  <view class="notify">
    <default-header :navTitle="'通知'" />
    <view class="main _main">
      <!-- 顶部标签栏 -->
      <u-tabs :list="tabsList" @click="changeTabs" :current="pageCurrent"></u-tabs>
      <swiper class="tabs-page" :current="pageCurrent" @change="changePage">
        <swiper-item v-for="i in 4" :key="i">
          <view class="tabs-page-item">
            <notify-msg-list :current="i - 1" />
          </view>
        </swiper-item>
      </swiper>
    </view>
  </view>
</template>

<script>
import DefaultHeader from "../../component/MyHeader/DefaultHeader.vue";
import NotifyMsgList from '../../component/NotifyMsg/NotifyMsgList.vue';
export default {
  name: "notify",
  components: { DefaultHeader, NotifyMsgList },
  data() {
    return {
			pageCurrent: 0,
      tabsList: [
        {
          name: "收到的赞",
        },
        {
          name: "评论与回复",
        },
        {
          name: "@我的",
        },
        {
          name: "关注",
        },
      ],
    };
  },
  onLoad: function (option) { //option为object类型，会序列化上个页面传递的参数
		this.pageCurrent = option.current;
	},
	methods: {
		changePage(e) {
			this.pageCurrent = e.detail.current;
		},
		changeTabs(e){
			this.pageCurrent = e.index
		}
	}
};
</script>

<style lang='scss' scoped>
uni-swiper{
	height: calc(100% - 44px);
}
.notify {

	.main {
		.tabs-page {
			.tabs-page-item {
        box-sizing: border-box;
        overflow-y: scroll;
        box-shadow: 0 0 2px #ececec inset;
				height: 100%;
        padding-bottom: 10px;
			}
		}
	}
}
</style>