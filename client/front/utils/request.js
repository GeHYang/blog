// 对axios进行二次封装
import axios from 'axios';
import store from '../store/index';

import settle from 'axios/lib/core/settle';
import buildURL from 'axios/lib/helpers/buildURL';

// 解决uniapp 适配axios请求，避免报adapter is not a function错误
axios.defaults.adapter = config => {
	return new Promise((resolve, reject) => {
		uni.request({
			method: config.method.toUpperCase(),
			url: config.baseURL + buildURL(config.url, config.params, config.paramsSerializer),
			header: {...config.headers},
			data: config.data,
			dataType: config.dataType,
			responseType: config.responseType,
			sslVerify: config.sslVerify,
			complete: function complete(response) {
				response = {
					data: response.data,
					status: response.statusCode,
					errMsg: response.errMsg,
					header: response.header,
					config: config
				};
				settle(resolve, reject, response);
			}
		})
	})
}


// 创建axios实例
const http = axios.create({
  // 请求前缀
  baseURL: 'http://192.168.0.4:32010/front',
  timeout: 10000,
  method: 'post',// 配置默认请求方式
  headers: {'Content-Type': 'application/json'}
});

// 添加请求拦截器
http.interceptors.request.use(function (config) {
	
  config.headers.Authorization = store.state.user.token;
  // 在发送请求之前做些什么
  return config;
}, function (error) {
  // 对请求错误做些什么
  return Promise.reject(error);
});

// 添加响应拦截器
http.interceptors.response.use(function (response) {
  // 对返回结果进行处理
  const data = response.data;
	// console.log(response);
  // 判断返回状态码是否为成功
  if(data.code >= 40000){
    // 请求失败
    uni.showToast({
      title: data.msg,
      duration: 2000,
      icon: "error"
    });
    return Promise.reject(data.msg);
  } else if(!data.flag){
    uni.showToast({title: "请求失败", icon: 'none'});
    return Promise.reject(data.flag);
  }

  return data;
}, function (error) {
  // 超出 2xx 范围的状态码都会触发该函数。
  // 对响应错误做点什么
  return Promise.reject(error);
});

// 暴露出去
export default http;