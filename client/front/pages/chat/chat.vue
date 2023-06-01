<template>
  <!-- 聊天页面 -->
  <view class="chat">
    <default-header :navTitle="toUser.nickName" :option="{url: '/pages/message/message', tabbar: true}" />
    <view class="main _main">
      <!-- 提示Toast -->
      <u-toast ref="tipsToast"></u-toast>
      <!-- 聊天列表 -->
      <scroll-view :scroll-top="scrollTop" scroll-y="true" class="chat-list">
        <chat-message
          v-for="(message, index) in chat.messageList"
          :message="message"
          :key="index"
        />
      </scroll-view>
      <!-- 聊天输入框 -->
      <view class="chat-in" :class="showEmojiBox && 'chat-in-emoji'">
        <u-row>
          <u-col span="2">
            <view class="voice">
              <view @click="showTalk" class="voice-icon">
                <text
                  class="iconfont"
                  :class="isVoice ? 'icon-jianpan' : 'icon-saying'"
                  :style="{ 'font-size': !isVoice ? '16px' : '26px' }"
                ></text>
              </view>
            </view>
          </u-col>
          <u-col span="8">
            <view
              class="chat-input"
              v-if="!isVoice"
              @click="showEmojiBox = false"
            >
              <base-input v-model="inputText" :emoji.sync="emoji">
                <template slot="suffix">
                  <text
                    @click.stop="showEmojiBox = !showEmojiBox"
                    class="iconfont icon-biaoqing emoji"
                  ></text>
                </template>
              </base-input>
            </view>
            <view
              v-else
              class="talk-box"
              :class="showTalkOverlay && 'talk-box-click'"
              @touchend="moveEnd"
              @touchstart="longClick"
              @touchmove="moveTouch"
            >
              <!-- 按住说话 -->
              <view class="talk">按住&nbsp;说话</view>
            </view>
          </u-col>
          <u-col span="2">
            <view class="send-btn">
              <view
                @click="sendMsg"
                class="btn"
                :class="inputText.length > 0 ? '' : 'btn-no-input'"
                v-if="!isVoice"
              >
                发送
              </view>
            </view>
          </u-col>
        </u-row>
        <!-- emoji列表 -->
        <emoji-list
          @clickEmoji="addEmoji2Input"
          v-show="showEmojiBox"
        ></emoji-list>
      </view>
      <!-- 长按说话遮罩层 -->
      <u-overlay :show="showTalkOverlay" @click="showTalkOverlay = false">
        <view class="talk-time">
          <view
            class="voice-in-box"
            :class="showCancel && 'voice-in-box-close'"
          >
            <view v-if="!showCancel" class="voice-icon">
              <view class="voice-animate">
                <p style="--i: 1"></p>
                <p style="--i: 2"></p>
                <p style="--i: 3"></p>
                <p style="--i: 4"></p>
                <p style="--i: 5"></p>
                <p style="--i: 6"></p>
                <p style="--i: 7"></p>
                <p style="--i: 8"></p>
                <p style="--i: 9"></p>
                <p style="--i: 10"></p>
              </view>
            </view>
            <view v-else class="voice-icon">
              <u-icon name="close" color="#fff" size="30" />
            </view>
          </view>
        </view>
      </u-overlay>
    </view>
  </view>
</template>

<script>
import ChatMessage from "../../component/ChatMessage";
import DefaultHeader from "../../component/MyHeader/DefaultHeader.vue";
import EmojiList from "../../component/EmojiList";
import BaseInput from "../../component/BaseInput/BaseInput.vue";
import { getUserrById } from '../../api/user';
import { chat, get } from '../../api/chat';
import { mapState } from 'vuex';

export default {
  components: {
    DefaultHeader,
    ChatMessage,
    EmojiList,
    BaseInput,
  },
  name: "chat",
  data() {
    return {
      toUser: {},
      inputText: "", // 输入框内容
      isVoice: false, // 是否显示按住说话按钮
      showTalkOverlay: false, // 显示遮罩层
      touches: null, // 存储初次点击按住说话按钮位置
      showCancel: false, // 显示取消按钮
      clickDuration: false, // 点击时长是否大于等于1s
      voiceDuration: 0, // 语音时长
      voiceDurationFun: null,
      timeoutFun: null,
      isTimeout: false, // 语音消息是否超时
      showEmojiBox: false, // 是否显示emoji盒子
      emoji: {},
      scrollTop: 0, // 页面滚动距离
    };
  },
  onLoad: function (options) {
    this.toUser.id = options.id;
    // 监听dynamicDetail事件，获取上一页面通过eventChannel传送到当前页面的数据
    const eventChannel = this.getOpenerEventChannel();
    eventChannel.on("chat", (user) => {
      this.toUser = user;
    });
    
    // 监听键盘事件
    uni.onKeyboardHeightChange((res) => {
      if (res.height > 0) {
        // 当键盘弹起时关闭emoji盒子
        this.showEmojiBox = false;
      }
    });
  },
  onShow(){
    this.$store.commit("SET_CHAT_ID", this.toUser.id);
  },
  beforeDestroy(){
    this.$store.commit("SET_CHAT_ID", "");
  },
  async mounted(){
    if(!this.toUser.username){
      // 请求后端获取用户信息
      this.byId(this.toUser.id)
    }
    let sqliteRes = await this.getMessageBySqlite()
    for(const r of sqliteRes){
      r.toUser = JSON.parse(r.toUser);
    }
    this.$store.commit("GET_CHAT_MESSAGE", {data: sqliteRes, flag: true});
    // 请求聊天记录
    let res = await get(null, this.toUser.id);
    // 提交到chat仓库
    this.$store.commit("GET_CHAT_MESSAGE", {data: res.data.data, flag: false});
    this.scrollBottom();
  },
  computed: {
    ...mapState({
      chat: state => state.chat,
      user: state => state.user
    })
  },
  watch: {
    
  },
  methods: {
    showTalk() {
      this.isVoice = !this.isVoice;
    },
    longClick(e) {
      // 触发振动事件
      uni.vibrateShort();
      // 将初次点击的位置保存起来
      this.touches = e.touches[0];
      this.showTalkOverlay = true;
      this.showCancel = false;
      this.clickDuration = false;
      this.voiceDuration = 1; // 语音时长
      this.isTimeout = false;
      this.timeoutFun = setTimeout(() => {
        this.clickDuration = true;
        this.voiceDurationFun = setInterval(() => {
          ++this.voiceDuration;
          if (this.voiceDuration === 59) {
            this.moveEnd(null, true);
          }
        }, 1000);
      }, 1000);
    },
    // 点击结束触发事件
    moveEnd(e, flag) {
      if (this.isTimeout) {
        return;
      }
      this.showTalkOverlay = false; // 关闭遮罩层
      clearInterval(this.voiceDurationFun);
      clearTimeout(this.timeoutFun)
      if (!flag && !this.clickDuration) {
        // 没有长按500ms触发
        this.$refs.tipsToast.show({
          duration: 1000,
          message: "语音时长过短！！",
        });
        return;
      }
      // 拿到手指松开时的位置
      let changedTouches = !flag && e.changedTouches[0];
      if (!flag && this.touches.pageY - changedTouches.pageY > 50) {
        // 取消语音消息
      } else {
        // 发送一条语音消息
        let data = {
          isMe: true,
          avatar: "../../static/headIcon.jpeg",
          message: this.voiceDuration,
          type: "voice", // 文字消息
        };
        this.messageList.push(data);
      }
      if (flag) {
        this.isTimeout = true;
        // 语音消息不宜过长
        this.$refs.tipsToast.show({
          duration: 1000,
          message: "语音消息过长",
        });
      }
    },
    // 滑动时触发，判断是否为取消，显示取消按钮
    moveTouch(e) {
      let touches = e.touches[0]; // 得到滑动位置
      if (this.touches.pageY - touches.pageY > 50) {
        this.showCancel = true;
      } else {
        this.showCancel = false;
      }
    },
    // 消息发送
    async sendMsg() {
      if (this.inputText == "") return;
      // 构建聊天请求数据
      let chatDto = {
        content: this.inputText,
        recvId: this.toUser.id,
        type: "text",
      };
      this.inputText = "";
      let timeRes = await this.sendTimeMsg();
      if(timeRes !== 1){
        this.addToSqlite(timeRes.data);
        this.$store.dispatch("onMessage", timeRes.data);
      }
      let res = await chat(chatDto);
      this.addToSqlite(res.data);
      this.$store.dispatch("onMessage", res.data);
      this.scrollBottom();
    },
    // 将emoji添加到输入框
    addEmoji2Input(emoji) {
      this.emoji = emoji;
    },
    // 根据用户id查询用户信息
    async byId(userId){
      let res = await getUserrById(userId);
      if(res.flag){
        this.toUser = res.data;
      }
    },
    // 添加到聊天列表
    addToSqlite(data) {
      let item = {
        toId: data.toUser.id,
        user: JSON.stringify(data.toUser),
        lastMsg: data.content,
        lastMsgTime: data.createTime,
        unread: 0
      }
      this.$sqlite.chatList.add(item);
    },
    // 从sqlite获取聊天信息
    getMessageBySqlite(){
      return this.$sqlite.message.page(this.toUser.id);
    },
    // 页面滚动到底部
    scrollBottom(){
      let view = uni.createSelectorQuery().in(this).select(".chat-list");

      this.$nextTick(() => {
        view.fields({
          size: true,
          scrollOffset: true
        }, data => {
            this.scrollTop = data.scrollHeight - data.height + 10
        }).exec();
      });
    },
    // 判断发送消息时间与最后一条信息时间间隔是否大于五分钟，大于则发送一条时间消息
    sendTimeMsg(){
      let lastTime = 0;
      if(this.chat.messageList.length > 0){
        let last = this.chat.messageList[this.chat.messageList.length - 1];
        lastTime = new Date(last.createTime).getTime();
      }
      let nowTime = new Date().getTime();
      if(lastTime === 0 || nowTime - lastTime >= 30 * 1000){
        // 构建聊天请求数据
        let chatDto = {
          content: uni.$u.timeFormat(nowTime, 'yyyy-mm-dd hh:MM:ss'),
          recvId: this.toUser.id,
          type: "time",
        };
        return chat(chatDto);
      }
      return 1;
    }
  },
};
</script>

<style lang='scss' scoped>
.chat {
  .main {
    .chat-list {
      box-sizing: border-box;
      height: calc(100% - 45px);
      padding: 0px 20px 0 20px;
    }

    .chat-in {
      padding: 0 20px;
      height: 45px;
      border-top: 1px solid #ececec;

      .voice {
        height: 45px;

        .voice-icon {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 35px;
          height: 35px;
          margin-top: 5px;
          border-radius: 50%;
          background-color: rgb(242, 242, 242);
          color: #5b5b5b;

          &:active {
            background-color: #ededed;
          }
        }
      }

      .chat-input {
        height: 45px;
        display: flex;
        align-items: center;

        .emoji {
          font-size: 25px;
          color: #9e9e9e;
        }
      }

      .talk-box {
        text-align: center;
        height: 39px;
        box-sizing: border-box;
        border: 1px solid #ececec;

        .talk {
          line-height: 39px;
        }
      }

      .talk-box-click {
        background-color: #ececec;
      }

      .send-btn {
        display: flex;
        align-items: center;
        height: 45px;
        padding: 0;
        padding-left: 10px;
        box-sizing: border-box;

        .btn {
          height: 30px;
          line-height: 30px;
          width: 100%;
          font-size: 12px;
          background-color: rgb(19, 175, 19);
          color: white;
          text-align: center;

          &:active {
            background-color: rgb(2, 97, 2);
          }
        }

        .btn-no-input {
          border: 1px solid #ececec;
          color: rgb(73, 73, 73);
          background-color: rgb(229, 229, 229);

          &:active {
            background-color: rgb(229, 229, 229);
          }
        }
      }
    }

    .chat-in-emoji {
      transform: translateY(-250px);
      background-color: white;
      transition: all 0.5s;
    }

    .talk-time {
      height: 100%;
      width: 100%;
      display: flex;
      justify-content: center;
      position: relative;

      .voice-in-box {
        position: absolute;
        bottom: 30%;
        width: 100px;
        padding: 20px;
        background-color: #a0ea6f;
        border-radius: 15px;

        .voice-icon {
          display: flex;
          justify-content: center;
          font-size: 30px;

          .voice-animate {
            display: flex;
            align-items: center;
            height: 30px;

            p {
              width: 2px;
              height: 5px;
              margin: 0 3px;
              background-color: white;
              animation: voice-frames 1s infinite;
              animation-delay: calc(var(--i) * 0.1s);
            }

            @keyframes voice-frames {
              0% {
                height: 5px;
              }

              50% {
                height: 30px;
              }

              100% {
                height: 5px;
              }
            }
          }
        }

        &::after {
          content: "";
          position: absolute;
          bottom: -20px;
          left: 50%;
          transform: translateX(-50%);
          width: 0px;
          height: 0px;
          border: 10px solid transparent;
          border-top-color: #a0ea6f;
        }
      }

      .voice-in-box-close {
        background-color: red;

        &::after {
          border-top-color: red;
        }
      }
    }
  }
}
</style>
