<template>
	<view class="userEdit">
		<default-header :navTitle="'设置'"/>
		<view class="main">
			<u-cell-group>
          <u-cell title="编辑资料" @click="toEditInfo" :isLink="true" arrow-direction="right"></u-cell>
          <u-cell title="修改密码" @click="showUPassPopup = true" :isLink="true" rightIcon="lock" arrow-direction="right"></u-cell>
          <u-cell title="更换邮箱" @click="updateEmail" :value="emailAndPhone.email"></u-cell>
          <u-cell title="更换手机号" @click="updatePhone" :value="emailAndPhone.phoneNumber"></u-cell>
			</u-cell-group>
			<my-popup :popupContent="popupContent" :showPopup.sync="showPopup"></my-popup>
			<view class="logout">
				<u-button type="error" @click="logout" text="退出登录"></u-button>
			</view>
			<!-- 修改密码弹窗 -->
			<u-popup :show="showUPassPopup" @close="closePopup" mode="center" :closeable="true">
				<view class="u-pass-box">
					<view class="title">修改密码</view>
					<u--input :customStyle="inputStyle" v-model="pass.oldPass" type="password" placeholder="原密码" clearable></u--input>
					<u--input :customStyle="inputStyle" v-model="pass.newPass" type="password" placeholder="新密码" clearable></u--input>
					<u--input :customStyle="inputStyle" v-model="pass.newPass1" type="password" placeholder="确认密码" clearable></u--input>
					<button class="btn" @click="updatePass">确认修改</button>
				</view>
			</u-popup>
		</view>
	</view>
</template>

<script>
import DefaultHeader from '../../component/MyHeader/DefaultHeader.vue';
import MyPopup from '../../component/MyPopup/MyPopup.vue';
import { mapState } from 'vuex';
import { updatePass, logout } from '../../api/user';

export default {
	name: "userEdit",
	components: {DefaultHeader, MyPopup},
	data() {
		return {
			showPopup: false,
			showUPassPopup: false,
			popupContent: {
				oldInput: "",
				placeholder: "",
				title: ""
			},// 弹窗内容
			inputStyle: {
				margin: "15px",
				width: "60vw"
			},
			// 密码
			pass: {
				oldPass: "",
				newPass: "",
				newPass1: ""
			}
		};
	},
	computed: {
		...mapState({
			userInfo: state => state.user.userInfo
		}),
		emailAndPhone(){
			return {
				email: this.userInfo?.email,
				phoneNumber: this.userInfo?.phoneNumber
			}
		}
	},
	methods: {
		updatePhone(){
			this.popupContent = {
				oldInput: "188*****888",
				placeholder: "请输入新手机号",
				title: "手机号修改"
			}
			this.showPopup = true;
		},
		updateEmail(){
			this.popupContent = {
				oldInput: "234****@qq.com",
				placeholder: "请输入新邮箱",
				title: "邮箱修改"
			}
			this.showPopup = true;
		},
		closePopup() {
			this.showUPassPopup = false;
		},
		// 跳转编辑资料页面
		toEditInfo(){
			uni.navigateTo({
				url: "/pages/editInfo/editInfo"
			})
		},
		logout(){
			// 发起退出登录请求
			logout().then(() => {
				this.$store.dispatch("logout");
			}).catch(() => {
				this.$store.dispatch("logout");
			})
			
		},
		// 修改密码
		async updatePass(){
			let regx = /\w{6,15}/;
			if(!regx.test(this.pass.oldPass) || !regx.test(this.pass.newPass)){
				uni.showToast({
					title: "密码长度需要6-15位！！",
					icon: "none"
				});
				return;
			}
			if(this.pass.newPass != this.pass.newPass1){
				uni.showToast({
					title: "两次新密码不一致",
					icon: "none"
				});
				return;
			}
			const res = await updatePass(this.pass.oldPass, this.pass.newPass);
			uni.showToast({
				title: res.msg,
				icon: "success"
			});
			this.closePopup();
		}
	}
};
</script>

<style lang='scss' scoped>
.userEdit {

	.main {
		padding: 20px;

		.logout {
			padding: 20px 80px;
		}
		::v-deep .u-popup__content {
			background-color: transparent;
		}
		.u-pass-box {
			padding: 10px;
			border-radius: 10px;
			background-color: white;

		.title {
      padding: 10px 0;
      font-size: 20px;
      font-weight: 600;
      text-align: center;
    }
			.btn{ 
				margin-top: 15px;
        width: 50%;
        background-color: #5ac725;
        color: white;

        &:active {
          background-color: #43931b;
        }
			}
		}
	}
}
</style>