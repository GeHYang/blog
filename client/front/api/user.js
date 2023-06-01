// 用户操作接口
import http from '../utils/request';
// 请求数据序列化工具
import qs from 'qs';

/**
 * 获取邮箱验证码
 * @param {*} email 邮箱号
 * @returns 
 */
export const getEmailCode = (email) => {
  return http({
    url: "/user/emailCode",
    method: 'post',
    data: qs.stringify({email}),
    headers: {"Content-Type": "application/x-www-form-urlencoded"}
  })
}
/**
 * 注册接口
 * @param {*} data 
 * @returns 
 */
export const register = (data) => {
  return http({
    url: "/user/emailRegister",
    data: data
  })
}
/**
 * 登录接口
 * @param {*} data 
 * @returns 
 */
export const login = (data) => {
  return http({
    url: "/user/login",
    data: data
  })
}
/**
 * 封面上传
 * @param {*} url 
 * @param {*} token 
 * @returns 
 */
export const editCover = (url, token) => {
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: 'http://192.168.0.4:32010/front/user/editCover', // 仅为示例，非真实的接口地址
      filePath: url, // 文件
      name: 'file', // 参数名
      header: {
        "Authorization": token
      },
      success: (res) => {
        const data = JSON.parse(res.data);
        // 校验状态码
        if(data.code >= 40000){
          // 请求失败
          uni.showToast({
            title: data.msg,
            duration: 2000,
            icon: "error"
          });
          reject(data.msg);
        }

        resolve(data);
      },
      fail(){
        reject("文件上传失败");
      }
    });
  });
}
/**
 * 修改用户信息
 * @param {*} url 
 * @param {*} userDto 
 * @returns 
 */
export const editInfo = (url, userDto, token) => {
  if(url){
    return new Promise((resolve, reject) => {
      uni.uploadFile({
        url: 'http://192.168.0.4:32010/front/user/editInfo', // 仅为示例，非真实的接口地址
        filePath: url, // 文件
        name: 'avatar', // 参数名
        formData: {
          userDto: JSON.stringify(userDto)
        },
        header: {
          "Authorization": token
        },
        success: (res) => {
          resolve(JSON.parse(res.data));
        },
        fail(){
          reject("修改失败");
        }
      });
    });
  } else {
    return http({
      url: "/user/editInfo",
      method: "post",
      data: {
        userDto: JSON.stringify(userDto)
      },
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      }
    })
  }
}
/**
 * 修改密码
 * @param {*} oldPass 
 * @param {*} newPass 
 * @returns 
 */
export const updatePass = (oldPass, newPass) => {
  let formData = new FormData();
  formData.append("oldPass", oldPass);
  formData.append("newPass", newPass);
  return http({
    url: "/user",
    method: "patch",
    data: formData,
    headers: {
      "Content-Type": "multipart/form-data"
    }
  })
}
/**
 * 退出登录
 * @returns 
 */
export const logout = () => {
  return http({
    url: "/user/logout",
    method: 'get'
  })
}
/**
 * 根据用户id查询用户信息
 * @param {*} userId 
 * @returns 
 */
export const getUserrById = (userId) => {
  return http.get("/user/" + userId);
}