<template>
  <view class="comment" v-if="JSON.stringify(comment) != '{}'">
    <view class="left">
      <image
        :style="{
          width: isParent ? '35px' : '25px',
          height: isParent ? '35px' : '25px',
        }"
        :src="comment.user.avatar"
      ></image>
    </view>
    <view class="right">
      <view class="username">{{ comment.user.nickName }}</view>
      <rich-text class="content" :nodes="msg"></rich-text>
      <view class="time">
        <text>{{ createTime }}</text>
        <text @click="recvComment">回复</text>
      </view>
      <view class="children"><slot /></view>
    </view>
  </view>
</template>

<script>
import { emojiList } from '../../utils/emojiData';
export default {
  name: "Comment",
  props: {
    isParent: {
      type: Boolean,
      default: false,
    },
    comment: {
      type: Object,
      default: () => ({}),
    },
  },
  computed: {
    msg: function () {
      let reg = /\[(.+?)\]/g; // 匹配[xxx]正则表达式
      let regData = this.comment.content.match(reg);
      let comment = JSON.parse(JSON.stringify(this.comment));// 深拷贝
      if (regData) {
        for (const emoji of emojiList) {
          let index = regData.indexOf(emoji.regName);
          if (index != -1) {
            let imgStr = `<img src='${emoji.src}' style="width: 20px;height:20px;" />`;
            regData[index] = regData[index].replace("[", "\\[");
            regData[index] = regData[index].replace("]", "\\]");
            let regx = new RegExp(`${regData[index]}`, "g");
            comment.content = comment.content.replace(regx, imgStr);
          }
        }
        // 将换行替换成br标签
        let regx = new RegExp("\n", "g");
        comment.content = comment.content.replace(regx, "<br>");
      }
      
      // 添加回复人信息
      if(comment.replyUser){
        comment.content = `<span style="color: #ccc;">回复
        <a style="color: blue;">@${comment.replyUser.nickName}</a>
        :
        </span>${comment.content}`;
      }
      return comment.content;
    },
    createTime: function() {
      return this.parseDate(new Date(), new Date(this.comment.createTime));
    }
  },
  data() {
    return {};
  },
  methods: {
    recvComment() {
      this.$bus.$emit("recv", true); // 往base-input发起焦点获取
      let commentInfo = {
        id: this.comment.id,
        pid: this.comment.pid,
      };
      this.$emit("recvComment", commentInfo);
    },
    
  },
};
</script>

<style lang='scss' scoped>
.comment {
  display: flex;
  padding: 5px 0px;

  .left {
    padding: 0 5px;
    image {
      border-radius: 50%;
    }
  }

  .right {
    width: calc(100% - 45px);
    display: flex;
    flex-direction: column;
    .username {
      color: #9fbfff;
      line-height: 25px;
      width: 100%;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .content {
      padding: 5px 0;
      font-size: 14px;
      color: #878787;
      div {
        img {
          vertical-align: middle; // 图片与文字平齐
        }
      }
    }
    .time {
      font-size: 12px;
      color: #cacaca;
      text:nth-child(1) {
        margin-right: 15px;
      }
    }
  }
}
</style>