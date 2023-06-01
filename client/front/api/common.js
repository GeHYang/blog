// 存放所有公共请求


// 图片上传请求
export const uploadImage = (url) => {
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: 'http://192.168.0.4:32002/upload', // 仅为示例，非真实的接口地址
      filePath: url, // 文件
      name: 'file', // 参数名
      success: (res) => {
        resolve(JSON.parse(res.data));
      },
      fail(){
        reject("文件上传失败");
      }
    });
  });
}