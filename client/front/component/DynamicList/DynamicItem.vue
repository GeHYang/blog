<template>
  <!-- 动态列表项 -->
  <view class="dynamic-item" v-if="dynamicDetail.user && JSON.stringify(dynamicDetail.user) != '{}'">
    <view class="left">
      <!-- 头像 -->
      <image :src="dynamicDetail.user.avatar" @click.stop="toUserDetail(dynamicDetail.user)"></image>
    </view>
    <view class="right">
      <!-- 用户信息 -->
      <view class="user-info">
        <!-- 用户名 -->
        <view class="name" @click.stop="toUserDetail(dynamicDetail.user)">{{ dynamicDetail.user.nickName }}</view>
        <view class="time">{{ time }}</view>
      </view>
      <!-- 动态文本信息 -->
      <view class="dynamic-text">{{ dynamicDetail.content }}</view>
      <!-- 动态图片 -->
      <view class="dynamic-images">
        <view class="dynamic-images-box">
          <image
            mode="widthFix"
            v-for="(item, index) in dynamicDetail.imgs"
            :key="index"
            :src="item"
            @click.stop="showBigImgFun(item)"
            :style="{
              width:
                dynamicDetail.imgs.length === 2
                  ? '40%'
                  : dynamicDetail.imgs.length >= 3
                  ? '33%'
                  : '',
            }"
          ></image>
        </view>
      </view>
      <!-- 评论点赞 -->
      <view class="other">
        <view>
          <u-icon @click="recvComment" name="chat" color="#b5b5b5" size="30"></u-icon>
          {{dynamicDetail.commentCount}}
        </view>
        <view>
          <u-icon name="heart-fill" color="#b5b5b5" size="30"></u-icon>
        </view>
      </view>
    </view>
    <u-overlay
      :opacity="0.8"
      :duration="100"
      :show="showBigImg"
      @click.native.stop="showBigImg = false"
    >
      <view class="main big-image">
        <image mode="widthFix" :src="bigImgSrc" />
      </view>
    </u-overlay>
  </view>
</template>

<script>
export default {
  name: "DynamicItem",
  props: {
    "dynamicDetail": {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      showBigImg: false,
      bigImgSrc: "",
    };
  },
  computed: {
    time(){
      return this.parseDate(new Date(), new Date(this.dynamicDetail.createTime))
    }
  },
  methods: {
    showBigImgFun(src) {
      this.bigImgSrc = src;
      this.showBigImg = true;
    },
    recvComment() {
      this.$bus.$emit("recv", true);
      let commentInfo = {};
      this.$bus.$emit("recvComment", commentInfo);
    },
    /**前往用户详情 */
    toUserDetail(user){
      uni.navigateTo({
        url: '/pages/userDetail/userDetail',
        success: function(res) {
          // 通过eventChannel向被打开页面传送数据
          res.eventChannel.emit('getUserDetailByUserId', { data: user });
        }
      })
    },
  },
};
</script>

<style lang='scss' scoped>
.dynamic-item {
  box-sizing: border-box;
  width: 100%;
  padding: 10px 20px;
  display: flex;
  border-bottom: 1px solid #ececec;

  .left {
    width: 50px;
    overflow: hidden;

    image {
      width: 35px;
      height: 35px;
      border-radius: 50%;
    }
  }
  .right {
    overflow: hidden;
    box-sizing: border-box;
    width: calc(100% - 50px);
    .user-info {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-bottom: 5px;

      .name {
        font-size: 18px;
        color: #9fbfff;
      }
      .time {
        font-size: 12px;
        color: #b5b5b5;
      }
    }

    .dynamic-text {
      font-size: 15px;
    }
    .dynamic-images {
      padding: 5px 0;
      .dynamic-images-box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-wrap: wrap;
        image {
          width: 80%;
          padding: 10px 0;
        }
      }
    }

    .other {
      display: flex;
      justify-content: flex-end;
      view {
        padding: 0 2px;
        display: flex;
        align-items: center;
        font-size: 12px;
        color: #b5b5b5;
      }
    }
  }
}
</style>