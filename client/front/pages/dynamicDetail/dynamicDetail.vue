<template>
  <view class="dynamic-detail">
    <default-header :navTitle="'详情'" />
    <view class="main _main">
      <dynamic-item :dynamicDetail="dynamicDetail" />
      <!-- 评论列表 -->
      <view class="comment-list">
        <view class="comment-item">
          <comment
            :isParent="true"
            v-for="(parent, index) in commentList"
            :key="index"
            :comment="parent"
            @recvComment="recvComment"
          >
            <comment
              :isParent="false"
              v-for="(child, i) in parent.children"
              :key="i + '' + index"
              :comment="child"
              @recvComment="recvComment"
            ></comment>
            <u-loadmore 
              :status="pageQueryList[index] ? (pageQueryList[index].end ? 'nomore' : 'loadmore') : 'loadmore'" 
              color="#1CD29B" 
              line 
              fontSize="12" 
              @loadmore="loadmoreComment(parent, index)"
            />
          </comment>
          <u-loadmore 
              v-show="showBottomLoading"
              :status="parentPageQuery.end ? 'nomore' : 'loading'" 
              color="#1CD29B" 
              line 
              fontSize="12" 
              @loadmore="loadmoreComment(parent, index)"
            />
        </view>
      </view>
      <view class="chat-input" :class="showEmojiBox && 'chat-in-emoji'">
        <u-row>
          <u-col span="10">
            <base-input
              v-model="inputText"
              :emoji.sync="emoji"
              :placeholder="'评论一下吧'"
            >
              <template slot="suffix">
                <text
                  class="iconfont icon-biaoqing emoji"
                  @click.stop="showEmojiBox = !showEmojiBox"
                ></text>
              </template>
            </base-input>
          </u-col>
          <u-col span="2">
            <view class="send-btn">
              <view
                class="btn"
                :class="inputText.length > 0 ? '' : 'btn-no-input'"
                @click="sendComment"
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
    </view>
  </view>
</template>

<script>
import DynamicItem from "../../component/DynamicList/DynamicItem.vue";
import DefaultHeader from "../../component/MyHeader/DefaultHeader.vue";
import Comment from "../../component/Comment";
import BaseInput from "../../component/BaseInput/BaseInput.vue";
import EmojiList from "../../component/EmojiList";
import { byId } from '../../api/dynamic';
import { getCommentByDynamicId, comment, commentPage } from '../../api/comment';

export default {
  components: {
    DefaultHeader,
    DynamicItem,
    Comment,
    BaseInput,
    EmojiList,
  },
  name: "dynamicDetail",
  data() {
    return {
      showEmojiBox: false,
      inputText: "", // 输入框内容
      emoji: {},
      commentList: [],
      recvCommentInfo: "", // 回复评论信息，存储评论id及父评论id
      isSendComment: false,
      dynamicDetail: {},// 动态详情数据
      dynamicId: "", // 文章id
      pageQueryList: {},
      parentPageQuery: {  // 最外层评论分页数据
        current: 1,
        size: 10,
        end: false,
      },
      showBottomLoading: false, // 显示底部加载动画
    };
  },
  onLoad(option){
    this.dynamicId = option?.id;
  },
  onReachBottom(){
    if(this.parentPageQuery.end) return;
    this.showBottomLoading = true;
    this.parentPageQuery.current++;
    
    commentPage(this.dynamicDetail.id, this.parentPageQuery).then(({data}) => {
      this.commentList.push(...data.data);
      if(data.pages <= this.parentPageQuery.current){
        this.parentPageQuery.end = true;
      }
    })
  },
  created(){
    const eventChannel = this.getOpenerEventChannel();
    // 监听dynamicDetail事件，获取上一页面通过eventChannel传送到当前页面的数据
    eventChannel.on("dynamicDetail", (data) => {
      this.dynamicDetail = data.data;
    });
  },
  async mounted() {
    if(!this.dynamicDetail?.id){
      let res = await this.getDynamicByUrlId();
      this.dynamicDetail = res.data;
    }
    this.getComment();
    
    uni.onKeyboardHeightChange((res) => {
      if (res.height > 0) {
        // 当键盘弹起时关闭emoji盒子
        this.showEmojiBox = false;
      }
    });

    this.$bus.$on("clearFocus", (res) => {
      if (res && this.isSendComment) {
        this.recvCommentInfo = {};
      }
    });
  },
  methods: {
    // 将emoji添加到输入框
    addEmoji2Input(emoji) {
      this.emoji = emoji;
    },
    // 发送评论
    async sendComment() {
      this.isSendComment = true;
      // 构建请求模型
      let commentDto = {
        content: this.inputText,  // 评论内容
        dynamicId: this.dynamicDetail.id,  // 动态id
        pid: this.recvCommentInfo.pid,  // 父评论id
        remindUserId: null, // @的用户的id
        replyId: "",  // 回复的评论的id
      };
      // 如果pid为空，则pid为回复的评论的id
      if(!commentDto.pid){
        commentDto.pid = this.recvCommentInfo.id;
      } else {// 如果pid不为空, replyId为回复的评论的id
        commentDto.replyId = this.recvCommentInfo.id;
      }
      let res = await comment(commentDto);
      this.inputText = "";
      this.addComment(res.data);
      this.isSendComment = false;
      // 评论数量+1
      this.dynamicDetail.commentCount+=1
    },
    // 子组件点击回复
    recvComment(res) {
      this.recvCommentInfo = res;
    },
    // 根据地址栏的id获取动态
    async getDynamicByUrlId(){
      return byId(this.dynamicId);
    },
    // 获取评论信息
    async getComment(){
      let res = await getCommentByDynamicId(this.dynamicDetail.id);
      this.commentList = res.data;
    },
    // 发表评论渲染
    addComment(commentVo){
      // 如果pid不存在，则直接插入到评论列表的头部
      if(!commentVo.pid){
        this.commentList.unshift(commentVo);
        return;
      }
      // 如果pid存在，遍历到指定pid，插入到children头部
      for(const commentParent of this.commentList){
        if(commentParent.id === commentVo.pid){
          commentParent.children.unshift(commentVo);
          return;
        }
      }
    },
    // 加载更多评论
    async loadmoreComment(parent, index){
      // 构造请求参数
      if(!this.pageQueryList[index]){
        this.pageQueryList[index] = {
          current: 1,
          size: 5,
          queryModel: {
            id: parent.id
          },
          end: false,// 结束标志
        }
      } else {
        this.pageQueryList[index].current++;
      }
      
      if(this.pageQueryList[index].end) return;

      let res = await commentPage(this.dynamicDetail.id, this.pageQueryList[index]);
      let commentList = res.data.data;
      if(!commentList || !commentList.length){
        this.pageQueryList[index].current--;
        this.pageQueryList[index].end = true;
        this.pageQueryList = JSON.parse(JSON.stringify(this.pageQueryList))
        return
      }
      if(this.pageQueryList[index].current === 1){
        // 首页删除第一条
        commentList.splice(0, 1);
      }
      // 添加到评论集合中
      parent.children.push(...commentList)
      
    }
  },
};
</script>

<style lang='scss' scoped>
.dynamic-detail {
  .main {
    // overflow-y: scroll;
    overflow: initial;

    .comment-list {
      padding-bottom: 15px;
      .comment-item {
        padding: 0 20px 20px 20px;
      }
    }
    .chat-input {
      width: 100%;
      position: fixed;
      background-color: white;
      bottom: -5px;
      height: 45px;
      padding: 0 3px;
      box-sizing: border-box;

      .emoji {
        font-size: 25px;
        color: #9e9e9e;
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
  }
}
</style>
