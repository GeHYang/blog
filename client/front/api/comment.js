// 评论请求的文件
import http from '../utils/request';
/**
 * 根据动态id获取评论
 * @param {*} dynamicId 
 * @returns 
 */
export const getCommentByDynamicId = (dynamicId) => {
  return http.get("/comment/" + dynamicId);
}
/**
 * 发表评论
 * @param {*} commentDto 
 */
export const comment = (commentDto) => {
  return http({
    url: "/comment",
    data: commentDto
  })
}
/**
 * 根据动态id获取评论
 * @param {*} dynamicId 
 * @returns 
 */
 export const commentPage = (dynamicId, pageQuery) => {
  return http({
    url: "/comment/" + dynamicId,
    data: pageQuery,
  });
}