<template>
  <view class="emoji-list">
    <view>
      <u-grid :border="false" col="8">
        <u-grid-item v-for="(emoji, listIndex) in list" :key="listIndex">
          <image @click="clickEmoji(emoji)" class="emoji-icon" :src="emoji.src" />
        </u-grid-item>
      </u-grid>
      <u-toast ref="uToast" />
    </view>
  </view>
</template>

<script>
import { emojiList } from '../../utils/emojiData';
export default {
  name: "EmojiList",
  data() {
    return {
      list: emojiList,
    };
  },
	methods: {
    clickEmoji(emoji) {
      this.$emit("clickEmoji", emoji);
    }
  }
};
</script>

<style lang='scss' scoped>
.emoji-list {
  height: 250px;
  overflow: hidden;
  overflow-y: scroll;
  border: 1px solid #ececec;
  background-color: white;
  view {
    .emoji-icon {
      padding: 5px 0;
      width: 25px;
      height: 25px;
    }
  }
}
</style>