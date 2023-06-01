<template>
  <view class="edit-info">
    <default-header :navTitle="'编辑资料'" />
    <view class="main _main">
      <!-- 头像 -->
      <view class="avatar">
        <u-upload multiple :maxCount="1" @afterRead="afterRead"
          ><u-avatar :src="userInfoTemp.avatar" :size="80"></u-avatar
        ></u-upload>
        <view class="edit-icon-box"><u-icon color="#3c9cff" size="25" name="plus-circle-fill" /></view>
      </view>
      <!-- 基本信息 -->
      <view class="info">
        <!-- id -->
        <u-cell disabled title="ID" :value="userInfoTemp.id"></u-cell>
        <!-- 昵称 -->
        <u-cell
          title="昵称"
          :value="userInfoTemp.nickName"
          @click="showEditNick = !showEditNick"
          :isLink="true"
        ></u-cell>
        <!-- 性别 -->
        <u-cell
          title="性别"
          :value="userInfoTemp.sex ? '男' : '女'"
          :isLink="true"
          @click="showSexSheet = !showSexSheet"
        ></u-cell>
        <!-- 生日 -->
        <u-cell
          title="生日"
          :value="userInfoTemp.birthday || '暂未填写'"
          :isLink="true"
          @click="showBirthday = !showBirthday"
        ></u-cell>
        <view class="save-btn"
          ><u-button @click="saveInfo" text="保存" size="normal" type="primary"></u-button
        ></view>
      </view>
      <!-- 修改用户昵称模态框 -->
      <u-modal
        :show="showEditNick"
        :showCancelButton="true"
        closeOnClickOverlay
        @close="showEditNick = false"
        @confirm="confirmEditNick"
        @cancel="cancelEditNick"
      >
        <view class="editNickName">
          <u--input v-model="userInfoTemp.nickName" placeholder="请输入新昵称" border="surround"></u--input>
        </view>
      </u-modal>
      <!-- 性别选择菜单 -->
      <u-action-sheet
        :show="showSexSheet"
        :title="'性别'"
        :actions="[{ value: 1, name: '男' }, { value: 0, name: '女' }]"
        :closeOnClickOverlay="true"
        :closeOnClickAction="true"
        @close="showSexSheet = false"
        @select="editSex"
        :round="5"
      ></u-action-sheet>
      <!-- 生日选择框 -->
      <u-datetime-picker
        :show="showBirthday"
        @close="showBirthday = false"
        @confirm="editBirthday"
        @cancel="showBirthday = false"
        :minDate="0"
        :value="new Date(userInfo.birthday).getTime()"
        mode="date"
        closeOnClickOverlay
      ></u-datetime-picker>
    </view>
  </view>
</template>

<script>
import DefaultHeader from "../../component/MyHeader/DefaultHeader.vue";
import { mapState } from 'vuex';
import { editInfo } from '../../api/user';
export default {
  name: "editInfo",
  components: { DefaultHeader },
  data() {
    return {
      showSexSheet: false,
      showBirthday: false,
      showEditNick: false,
      fileList: [],
      userInfoTemp: {},
      updateAvatarFlag: false,
    };
  },
  computed: {
  // 使用对象展开运算符将 getter 混入 computed 对象中
    ...mapState({
      user: state => state.user,
      userInfo: state => state.user.userInfo
    })
  },
  created(){
    this.userInfoTemp = {...this.userInfo}
  },
  methods: {
    // 确认修改昵称
    confirmEditNick(){
      this.showEditNick = false;// 关闭模态框
      this.userInfo.nickName = this.userInfoTemp.nickName;// 昵称赋值
    },
    // 取消修改昵称
    cancelEditNick(){
      this.showEditNick = false;
      // 重置用户昵称
      this.userInfoTemp.nickName = this.userInfo.nickName;
    },
    // 修改性别
    editSex(e){
      this.userInfoTemp.sex = e.value
    },
    // 修改生日
    editBirthday(e){
      // 时间戳格式转换
      let date = new Date(e.value);
      let month = date.getMonth() + 1;
      let day = date.getDate();

      month = month > 9 ? month : '0' + month;
      day = day > 9 ? day : '0' + day;
      let birthday = `${date.getFullYear()}.${month}.${day}`;
      this.userInfoTemp.birthday = birthday;
      // 关闭弹框
      this.showBirthday = false;
    },
    // 头像修改
    afterRead(event) {
      this.updateAvatarFlag = true;
      this.userInfoTemp.avatar = event.file[0].url;
    },
    // 保存修改
    async saveInfo(){
      let url = null;
      if(this.updateAvatarFlag){
        url = this.userInfoTemp.avatar;
      }
      let birthday = this.userInfoTemp?.birthday;
      if(birthday) birthday = birthday.replace(/\./g, "-");
      let userDto = {...this.userInfoTemp, avatar: "", birthday};
      let res = await editInfo(url, userDto, this.user.token);
      uni.showToast({
				title: res.msg,
				duration: 1000,
				icon: "none"
			});
      this.$store.commit("UPDATE_INFO", {...userDto, avatar: res.data || this.userInfoTemp.avatar})
    }
  }
};
</script>

<style lang='scss' scoped>
.edit-info {
  padding: 10px 20px;

  .main {
    .avatar {
      position: relative;
      .edit-icon-box {
        position: absolute;
        left: calc(50% + 15px);
        top: calc(50% + 15px);
      }

      ::v-deep .u-upload__wrap {
        display: flex;
        justify-content: center;
      }
    }

    .info {
      margin-top: 10px;

      .save-btn {
        width: 50%;
        margin: 0 auto;
        margin-top: 30px;
      }
    }
  }
}
</style>