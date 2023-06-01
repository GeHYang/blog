<template>
	<!-- 聊天信息 -->
	<view class="chat-message">
		<!-- 对方的信息 -->
		<view class="to" v-if="!message.isMe && message.type!=='time'">
			<view class="left">
				<image class="avatar" :src="message.toUser.avatar"></image>
			</view>
			<view class="right">
				<!-- 文字信息内容 -->
				<view class="content">
					<!-- <text v-if="message.type==='text'">{{ msg }}</text> -->
					<rich-text class="richText" v-if="message.type==='text'" :nodes="msg"></rich-text>
					<!-- 语音消息内容 -->
					<view class="voice-content" v-else>
						<text class="iconfont icon-saying"></text>
						<b class="voice-conten-tips">{{ message.content }}"</b>
					</view>
				</view>

			</view>
		</view>
		<!-- 自己发送的信息 -->
		<view class="me" v-else-if="message.isMe && message.type!=='time'">
			<view class="left">
				<image class="avatar" :src="$store.state.user.userInfo.avatar"></image>
			</view>
			<view class="right">
				<!-- 内容 -->
				<view class="content">
					<!-- <text v-if="message.type==='text'">{{ msg }}</text> -->
					<rich-text class="richText" v-if="message.type==='text'" :nodes="msg"></rich-text>
					<!-- 语音消息内容 -->
					<view class="voice-content" v-else>
						<text class="iconfont icon-saying icon"></text>
						<b class="voice-conten-tips">{{message.content}}"</b>
					</view>
				</view>
			</view>
		</view>
		<!-- 时间消息 -->
		<view class="time-msg" v-else>
			{{timeMsg(msg)}}
		</view>
	</view>
</template>

<script>
import { emojiList } from '../../utils/emojiData';
	export default {
		name: "ChatMessage",
		props: ["message"],
		data() {
			return {};
		},
		computed: {
			"msg":function() {
				let reg = /\[(.+?)\]/g;// 匹配[xxx]正则表达式
				let regData = this.message.content.match(reg);
				if(!regData) return this.message.content;
				for(const emoji of emojiList){
					let index = regData.indexOf(emoji.regName);
					if(index != -1){
						let imgStr = `<img src='${emoji.src}' style="width: 20px;height:20px;" />`;
						regData[index] = regData[index].replace("[", "\\[");
						regData[index] = regData[index].replace("]", "\\]");
						let regx = new RegExp(`${regData[index]}`, 'g');
						this.message.content = this.message.content.replace(regx, imgStr);
					}
				}
				// 将换行替换成br标签
				let regx = new RegExp("\n", 'g');
				this.message.content = this.message.content.replace(regx, "<br>")
				return this.message.content;
			}
		},
		methods: {
			timeMsg(msg){
				let date = new Date(msg);
				let nowDate = new Date();
				if(date.getFullYear() != nowDate.getFullYear()){
					return uni.$u.timeFormat(date.getTime(), 'yyyy-mm-dd hh:MM');
				}else if(date.getDate() != nowDate.getDate()){
					return uni.$u.timeFormat(date.getTime(), 'mm-dd hh:MM');
				} else {
					return uni.$u.timeFormat(date.getTime(), 'hh:MM');
				}
			}
		},
	};
</script>

<style lang='scss' scoped>
	.chat-message {
		display: flex;
		flex-direction: column;
		width: 100%;
		margin: 10px 0;

		.to,
		.me {
			display: flex;
			align-items: center;
			max-width: 80%;

			.left {
				box-sizing: border-box;
				width: 50px;
				height: 50px;

				image {
					width: 45px;
					height: 45px;
					border-radius: 50%;
				}
			}

			.right {
				margin-left: 10px;

				.content {
					position: relative;
					min-height: 30px;
					padding: 5px 10px;
					display: flex;
					align-items: center;
					background-color: rgb(179, 206, 213);
					border-radius: 5px;
					position: relative;

					.richText{
						::v-deep div img{
							vertical-align: middle;
						}
					}

					.voice-content {
						min-width: 40px;

						.voice-conten-tips {
							position: absolute;
							right: -33px;
							width: 30px;
							font-size: 14px;
							color: #838383;
						}
					}

					&::after {
						content: "";
						position: absolute;
						top: 50%;
						transform: translateY(-50%);
						left: -18px;
						width: 0px;
						height: 0px;
						border: 10px solid transparent;
						border-right-color: rgb(179, 206, 213);
					}
				}
			}
		}

		.me {
			align-self: flex-end;
			flex-direction: row-reverse;

			.right {
				margin-left: 0;
				margin-right: 10px;

				.content {
					.voice-content {
						text-align: right;
						.icon {
							display: inline-block;
							transform: rotate(180deg);
						}
						.voice-conten-tips {
							right: 0px;
							left: -33px;
							text-align: right;
						}
					}

					&::after {
						content: "";
						left: calc(100% - 2px);
						border: 10px solid transparent;
						border-left-color: rgb(179, 206, 213);
					}
				}
			}
		}
		// 时间
		.time-msg {
			width: 100%;
			font-size: 12px;
			text-align: center;
			color: #838383;
		}
	}
</style>
