<template>
	<!-- 消息列表 -->
	<view class="message-list">
		<u-swipe-action>
			<u-swipe-action-item :options="options" v-for="(item, index) in chat.chatList" :key="index" :name="index"
				:ref="'swipeActionItem:' + index" @click="delMessageItem">
				<message-item @click.native="toDetail(item.user)" :messageItem="{ ...item, index: index }">
					<u-badge max="99" :value="item.unread"></u-badge>
				</message-item>
			</u-swipe-action-item>
		</u-swipe-action>
	</view>
</template>

<script>
	import MessageItem from "./MessageItem.vue";
	import {mapState} from 'vuex';
	import { getChatList } from '../../api/chat';

	export default {
		name: "MessageList",
		components: {
			MessageItem
		},
		data() {
			return {
				options: [{
					text: '删除',
					style: {
						backgroundColor: '#f56c6c',
					},
				}, ]
			};
		},
		created(){
			this.$bus.$on("onShow", () => {
				// 获取数据库中的列表信息
				this.getListBySqlite();
			});
			this.$bus.$on("onHide", () => {
				this.addToSqlite();
			})
		},
		computed: {
			...mapState({
				chat: state => state.chat
			})
		},
		methods: {
			toDetail(user) {
				this.$store.commit("CLEAR_UNREAD", user.id);
				uni.navigateTo({
					url: "/pages/chat/chat?id=" + user.id,
					success(res) {
						res.eventChannel.emit("chat", user);
					}
				});
			},
			delMessageItem({name}) {
				this.$store.commit("DEL_CHAT_ITEM", name);
				this.$refs["swipeActionItem:" + name][0].closeHandler();
			},
			// 从sqlite拿到列表信息
			async getListBySqlite(){
				let data = await this.$sqlite.chatList.getAll();
				for(const obj of data){
					obj.user = JSON.parse(obj.user);
				}
				this.$store.commit("INIT_CHAT_LIST", data);
				// 从后端获取聊天列表
				getChatList().then(res => {
					for(const item of res.data){
						this.$store.commit("ADD_CHAT_LIST", {item});
					}
				})
			},
			// 添加到聊天列表
			addToSqlite() {
				for(const item of this.chat.chatList){
					this.$sqlite.chatList.add({...item, user: JSON.stringify(item.user)});
				}
			}
		},
	};
</script>

<style lang='scss' scoped>
	::v-deep .u-swipe-action-item {
		touch-action: initial;
	}

	::v-deep .u-swipe-action {
		height: 100%;
		overflow: hidden;
		overflow-y: scroll;
	}

	.message-list {
		height: 100%;

		.tips {
			display: block;
			padding: 10px 0;
			width: 100%;
			text-align: center;
		}
	}
</style>
