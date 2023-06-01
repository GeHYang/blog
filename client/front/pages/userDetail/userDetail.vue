<template>
		<!-- 用户详情页面 -->
	<view class="user-detail">
		<default-header :navTitle="'用户动态'">
			<u-icon v-if="userInfo.id === $store.state.user.userInfo.id" name="more-dot-fill" size="20" @click="showSheet = true"/>
		</default-header>
		<view class="main" :class="isMe ? '_main' : ''">
			<!-- 用户信息 -->
			<view class="user-info" :style="{ 'background-image': userInfo.cover && 'url(' + userInfo.cover + ')' }">
				<!-- 粉丝数、关注量 -->
				<view class="count">
					<!-- 粉丝数 -->
					<view class="fans">
						<text class="num">0</text>
						<text>粉丝</text>
					</view>
					<!-- 关注量 -->
					<view class="concern">
						<text class="num">16</text>
						<text>关注</text>
					</view>
				</view>
				<!-- 用户信息 -->
				<view class="info">
					<view class="avatar">
						<image :src="userInfo.avatar" @click="showBigImg(userInfo.avatar)"></image>
					</view>
					<view class="detail">
						<view class="nickName">
							<text>{{ userInfo.nickName }}</text>
							<text>ID: {{ userInfo.username }}</text>
						</view>
						<view class="sex"><u-icon name="account-fill" :color="userInfo.sex ? 'blue' : 'red'" size="18" /></view>
					</view>
				</view>
			</view>
			<!-- 动态信息 -->
			<view class="dynamic-list">
				<user-dynamic-list v-for="(item, index) in userDetailList" :key="index" :dynamicList="item"/>
			</view>
		</view>
		<!-- 底部选项 -->
		<view class="footer" v-if="!isMe">
			<view class="concern"><u-icon name="man-add" :color="'red'" size="20" label="关注"/></view>
			<view class="chat" @click="toChat"><u-icon name="chat" size="20" label="私信"></u-icon></view>
		</view>
		<!-- 大图遮罩层 -->
		<u-overlay :show="showAvatar" @click="showAvatar = false">
			<view style="width: 100%;height: 100%;display: flex;align-items: center;justify-content: center;">
				<image :src="bigImgUrl"></image>
			</view>
		</u-overlay>
		<!-- 上拉列表 -->
		<u-action-sheet
			:show="showSheet" 
			:cancelText="''" 
			:closeOnClickOverlay="true" 
			@close="showSheet = false" 
			:round="15"
		>
			<u-upload
				multiple
				:maxCount="1"
				@afterRead="readCover"
			>
				<button class="updateCover">更换封面</button>
			</u-upload>
	</u-action-sheet>
	</view>
</template>

<script>
import DefaultHeader from '../../component/MyHeader/DefaultHeader.vue';
import UserDynamicList from '../../component/UserDynamicList/UserDynamicList.vue';
import { page } from '../../api/dynamic';
import { editCover } from '../../api/user';

export default {
  components: { DefaultHeader, UserDynamicList },
	name: "userDetail",
	data() {
		return {
			userDetailList: {},
			userInfo: {},
			bigImgUrl: "",
			showAvatar: false,
			showSheet: false,
		};
	},
	created(){
		uni.showLoading({
			title: "加载中",
			mask: true
		})
	},
	computed: {
		isMe(){
			return this.userInfo.id == this.$store.state.user.userInfo.id;
		}
	},
	async mounted (){
		await this.getUserDetailByUserId();
		if(this.userInfo.id){
			this.getDynamicByUserId(this.userInfo.id);
		}
		else{
			this.userInfo = this.$store.state.user.userInfo
			this.getDynamicByUserId(this.userInfo.id);
		}
	},
	methods: {
		async getDynamicByUserId(userId){
			let pageQuery = {
				current: 0, // 当前页数
				size: 10, // 页大小
				queryModel: {	// 查询条件
					"createBy": userId
				}
				
			}
			let res = await page(pageQuery);
			let data = res.data.data;
			if(data && data.length > 0)
				this.userInfo = data[0].user;
			this.userDetailList = this.dataHandle(data);
		},
		
		// 数据处理
		dataHandle(dataList){
			let result = [];
			for(const data of dataList){
				let year = data.createTime.split("-")[0];
				let month = data.createTime.split("-")[1];
				let day = data.createTime.split("-")[2];
				day = day.split(" ")[0];
				let flag = false;
				for(const r of result){
					if(r.year == year){
						r.detail.push({
							month,
							day,
							dynamic: data
						});
						flag = true;
						break;
					}
				}
				if(!flag){
					let item = {
						year,
						detail: [{
							month,
							day,
							dynamic: data
						}]
					};
					result.push(item);
				}
			}
			return result;
		},
		// 显示大图
		showBigImg(url) {
			this.showAvatar = true;
			this.bigImgUrl = url;
		},
		// 选择封面
		async readCover(e){
			this.showSheet = false;
			let url = e.file[0].url;
			let res = await editCover(url, this.$store.state.user.token);
			uni.showToast({
				title: res.msg,
				duration: 1000,
				icon: "success"
			});
			this.userInfo.cover = res.data;
		},
		// 前往聊天界面
		toChat(){
			uni.navigateTo({ 
        url: "/pages/chat/chat?id=" + this.userInfo.id,
        success: (res) => {
          res.eventChannel.emit("chat", this.userInfo);
        }
      });
		},
		// getUserDetailByUserId
		getUserDetailByUserId(){
			return new Promise((resolve) => {
				const eventChannel = this.getOpenerEventChannel();
				// 监听acceptDataFromOpenerPage事件，获取上一页面通过eventChannel传送到当前页面的数据
				eventChannel.on('getUserDetailByUserId', ({data}) => {
					this.userInfo = data;
					uni.hideLoading();
					resolve(true);
				});
				setTimeout(() => {
					uni.hideLoading();
					resolve(false);
				}, 2000);
			})
		}
	},

};
</script>

<style lang='scss' scoped>
.user-detail {
	.main {
		overflow-y: scroll;
		.user-info {
			z-index: 1;
			position: sticky;
			top: calc(-40% + 70px);
			height: 40%;
			background-image: url("../../static/4.jpeg");
			background-repeat: no-repeat;
			background-size: cover;

			.count {
				padding: 0 20px;
				height: calc(100% - 70px);
				display: flex;
				align-items: flex-end;
				justify-content: flex-end;
				flex-direction: column;

				.fans, .concern {
					display: flex;
					flex-direction: column;
					align-items: center;
					text {
						font-size: 12px;
						color: rgb(233, 233, 233);
					}
					.num {
						font-size: 18px;
						font-weight: 600;
					}
				}
			}

			.info {
				padding: 10px 20px;
				display: flex;
				height: 50px;

				.avatar {
					image {
						width: 50px;
						height: 50px;
						border-radius: 50%;
					}
				}

				.detail {
					display: flex;
					align-items: center;
					max-width: calc(100% - 50px);
					color: white;

					.nickName {
						display: flex;
						flex-direction: column;
						width: 90%;
						padding: 0 5px;
						text{
							text-overflow: ellipsis;
							white-space: nowrap;
							overflow: hidden;

							&:nth-child(2){
								font-size: 14px;
							}
						}
					}
				}
			}

		}
		.dynamic-list {
			height: 60%;
		}
	}
	.footer {
		height: 45px;
		border-top: 1px solid #ececec;
		display: flex;
		justify-content: center;

		view {
			box-sizing: border-box;
			width: 48%;
			display: flex;
			justify-content: center;
		}
	}
	.updateCover {
		width: 100vw;
		height: 40px;
		line-height: 40px;
		font-size: 14px;
		border: none;
		background: transparent;
		text-align: center;
		border-radius: 15px;
	}
}
</style>