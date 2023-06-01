/**
 * 日期处理
 * 1小时内：每分钟前
 * 1小时-1天：多少小时前
 * 1-3天：多少天前
 * 4天及以上月-日
 * 一年以上：年-月-日
 * @param {*} nowDate 
 * @param {*} dynamicDate 
 * @returns 
 */
export const parseDate = (nowDate, dynamicDate) => {
  let time = nowDate.getTime() - dynamicDate.getTime();
  if(time < 60 * 60 * 1000){// 1小时内
      return Math.ceil((time / 60 / 1000)) + "分钟前";
  } else if(time < 24 * 60 * 60 * 1000){
      return Math.ceil((time / 60 / 60 / 1000)) + "小时前";
  } else if(time < 4 * 24 * 60 * 60 * 1000){
      return Math.ceil((time / 24 / 60 / 60 / 1000)) + "天前";
  } else if(nowDate.getFullYear() == dynamicDate.getFullYear()){
      return `${dynamicDate.getMonth() + 1}-${dynamicDate.getDate()}`
  } else {
      return `${dynamicDate.getFullYear()}-${dynamicDate.getMonth() + 1}-${dynamicDate.getDate()}`
  }
}