/** 自封装storage */
// 获取
const get = (key, isObject = false) => {
  try {
    let value = uni.getStorageSync(key);
    if(isObject){
      // 字符串转json对象
      value = JSON.parse(value);
    }
    return value;
  } catch (e){
    return "";
  }
}
// 设置String类型
const set = (key, val, isObject = false) => {
  if(isObject){
    val = JSON.stringify(val); // 对象转json字符串存储
  }
  try {
    uni.setStorageSync(key, val);
    return true;
  } catch (e) {
    return false;
  }
}
// 删除
const del = (key) => {
  try {
    uni.removeStorageSync(key);
    return true;
  } catch (e) {
    return false;
  }
}

export default {
  get, set, del
}
