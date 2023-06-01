<template>
  <!-- 写说说 -->
  <view class="publish-dynamic">
    <default-header :navTitle="'写说说'">
      <text class="dh-btn" @click="publishDynamic">发布</text>
    </default-header>
    <view class="main _main">
      <view class="write">
        <u--textarea
          border="false"
          height="160"
          placeholder="请输入内容"
          :maxlength="500"
          v-model="content"
          count
        ></u--textarea>
      </view>
      <view class="upload-box">
        <u-upload
          :fileList="fileList"
          multiple
          :maxCount="10"
          :previewFullImage="true"
          @afterRead="afterRead"
          @delete="deletePic"
        ></u-upload>
      </view>
    </view>
  </view>
</template>

<script>
import DefaultHeader from "../../component/MyHeader/DefaultHeader.vue";
import { add } from '../../api/dynamic';
import { uploadImage } from '../../api/common';

export default {
  components: { DefaultHeader },
  name: "publishDynamic",

  data() {
    return {
      fileList: [],
      imgs: [],
      content: "",
    };
  },
  methods: {
    // 删除图片
    deletePic(event) {
      this.fileList.splice(event.index, 1);
      this.imgs.splice(event.index, 1);
    },
    // 1、新增图片
    async afterRead(event) {
      // 2、图片发送到服务器
      let res = await this.uploadImage(event.file[0].url);// 3、请求成功，得到反馈信息
      // 4、将图片路径存入图片列表
      this.fileList.push({url: res.data});
      // 5、将图片路径存储到图片列表，用于发布
      this.imgs.push(res.data)
    },
    // 图片上传方法
    uploadImage(url){
      return uploadImage(url);
    },
    // 发布动态
    async publishDynamic(){
      if(!this.content){
        return;
      }
      let dynamicInfo = {
        content: this.content, // 动态文本内容
        createBy: 1, // 发布者id
        imgs: this.imgs,// 图片集合
        remindId: "", // @的人
      }
      await add(dynamicInfo);
      uni.switchTab({
        url: "/pages/dynamic/dynamic", 
        success: () => {
          this.$bus.$emit("publish");
        }
      })
    }
  },
};
</script>

<style lang='scss' scoped>
.publish-dynamic {
  .dh-btn {
    color: #1989fa;
  }
  .main {
    padding: 0 20px;

    .write {
      margin: 10px 0;
      box-shadow: 0 0 5px #efefef;
    }

    .upload-box {
      display: flex;
      box-shadow: 0 0 5px #efefef;
      padding: 5px 5px;

      ::v-deep .u-upload__button,
      ::v-deep .u-upload__wrap__preview {
        margin: 0;
      }
    }
  }
}
</style>