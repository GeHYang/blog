<template>
  <view class="my-popup">
    <u-popup :show="showPopup" @close="closePopup" mode="center" :closeable="true">
      <view class="popup-box">
        <view class="title">{{ popupContent.title }}</view>
        <view class="old">
          <input type="text" class="old-input" disabled :value="popupContent.oldInput" />
          <button class="old-btn">获取验证码</button>
        </view>
        <view class="old-code">
          <u-code-input :maxlength="4"></u-code-input>
        </view>
        <view class="new">
          <input type="text" class="old-input" :placeholder="popupContent.placeholder" />
          <button class="old-btn">获取验证码</button>
        </view>
        <view class="new-code">
          <u-code-input :maxlength="4"></u-code-input>
        </view>
        <view class="confirm-btn">
          <button>确认修改</button>
        </view>
      </view>
    </u-popup>
  </view>
</template>

<script>
export default {
  name: "MyPopup",
  props: {
    showPopup: {
      type: Boolean,
      default: false
    },
    popupContent: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {};
  },
  methods: {
    closePopup(){
      this.$emit("update:showPopup", false)
    }
  }
};
</script>

<style lang='scss' scoped>
::v-deep .u-popup__content {
  background-color: transparent;
}
.my-popup {

  .popup-box {
    padding: 10px;
    overflow: hidden;
    border-radius: 15px;
    background-color: #fff;

    .title {
      padding: 10px 0;
      font-size: 20px;
      font-weight: 600;
      text-align: center;
    }

    .old, .new {
      display: flex;
      padding: 5px;
      .old-input {
        box-sizing: border-box;
        height: 40px;
        padding: 5px;
        border: 1px solid #ccc;
      }
      .old-btn {
        box-sizing: border-box;
        height: 40px;
        padding: 0 5px;
        margin: 0 5px;
        line-height: 40px;
        font-size: 14px;
      }
    }

    .old-code, .new-code {
      padding: 5px;
    }

    .confirm-btn {
      padding: 5px;

      button {
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