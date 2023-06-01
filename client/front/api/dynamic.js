// 动态请求的文件
import http from '../utils/request';


// 分页查询
export const page = function (pageQuery = {}) {
  return http({
    url: '/dynamic/page',
    data: pageQuery// 请求的参数
  });
}
// 动态发布
export const add = (dynamic) => {
  return http({
    method: 'post', // 请求方式
    url: '/dynamic', // 请求路径
    data: dynamic, // 请求数据
  });
}
/**
 * 根据动态id获取动态
 * @param {*} dynamicId 
 * @returns 
 */
export const byId = (dynamicId) => {
  return http({
    url: "/dynamic/" + dynamicId,
    method: 'get'
  })
}
/**
 * 删除动态
 * @param {*} dynamicId 
 */
export const delById = (dynamicId) => {
  return http({
    url: "/dynamic/" + dynamicId,
    method: 'delete'
  })
}