<template>
  <view class="container">
    <my-header />
    <view class="main">
      <view class="swiper">
        <u-swiper :circular="true" :list="list1" height="200" imgMode="heightFix"></u-swiper>
      </view>
      <view class="tags">
        <u-row>
          <u-col span="3" v-for="index in 4" :key="index">
            <view class="tag">
              <u-icon name="bag-fill" color="#2979ff" size="45"></u-icon>
            </view>
          </u-col>
        </u-row>
      </view>
      <!-- 推荐 -->
      <view class="recommend">
        <view class="recommend-title"> 推荐 </view>
        <view class="recommend-box">
          <recommend-list />
        </view>
      </view>
    </view>
    <my-footer :name="'index'" />
  </view>
</template>

<script>
import MyHeader from "../../component/MyHeader";
import RecommendList from "../../component/RecommendList";

export default {
  name: "home",
  components: { MyHeader, RecommendList },
  data() {
    return {
      list1: [
        "../../static/1.jpeg",
        "../../static/4.jpeg",
        "../../static/3.jpeg",
      ],
    };
  },
};
</script>

<style lang='scss' scoped>
.container {
  .main {
    overflow-x: hidden;
    overflow-y: scroll;
    box-sizing: border-box;
    padding: 0 20px;
    padding-top: 5px;

    .swiper {
      height: 200px;
      width: 100%;
      border-radius: 15px;
      overflow: hidden;
      box-shadow: 0 0 10px #ccc;
    }
    .tags {
      margin-top: 15px;

      .tag {
        height: 50px;
        width: 80px;
        box-shadow: 0 0 5px #ececec;
        border-radius: 15px;
        display: flex;
        justify-content: center;
        align-items: center;
        transition: all 0.5s;

        &:active {
          transform: scale(0.6);
        }
      }
    }

    .recommend {
      margin-top: 15px;

      .recommend-title {
        padding: 5px 0;
        margin-bottom: 15px;
        width: 80px;
        box-shadow: 0 0 5px #ececec;
        border-radius: 15px;
        display: flex;
        justify-content: center;
        align-items: center;
        transition: all 0.5s;

        &:active {
          transform: scale(0.6);
        }
      }
    }
  }
}
</style>