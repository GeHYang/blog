// chat_list表操作
import { createTable, dropTable } from './DDL';
import { selector,add, update, del } from './DML';
import store from '../store';
// 聊天列表表
const chatList = 
`create table if not exists chat_list(
  id varchar(21) not null,
  toId varchar(21) not null,
  user varchar(2000) not null,
  lastMsg varchar(2000) null,
  lastMsgTime varchar(20) null,
  unread int not null default 0
);`;
async function addObj(obj) {
  if(!obj.toId){
    console.error("缺少关键参数");
    return false;
  }
  // 如果数据库已存在，则修改信息
  let res = await selectByToId(obj.toId)
  if(res && JSON.stringify(res) != '{}' && JSON.stringify(res) != '[]'){
    return updateByToId(obj.toId, obj);
  }
  let sql = `insert into chat_list(id, toId, user, lastMsg, lastMsgTime, unread)
  values('${store.state.user.userInfo.id}','${obj.toId}', '${obj.user}', '${obj.lastMsg}', '${obj.lastMsgTime}'
  ,${obj.unread})`;
  return add(sql);
}
function selectByToId(toId) {
  const sql = `select * from chat_list where id='${store.state.user.userInfo.id}' and toId = '${toId}'`;
  return selector(sql, true);
}

function updateByToId(toId, obj) {
  // 构建sql语句
  let sql = "update chat_list";
  let keys = " set";
  // 如果obj存在id，则删除
  delete obj.id;
  delete obj.toId;
  // 插入键名
  for(const key of Object.keys(obj)){
    keys += " " + key + "='" + obj[key] + "',";
  }
  keys = keys.substring(0, keys.length - 1);
  sql += keys + " where id=" + "'" + store.state.user.userInfo.id + "' and toId='" + toId + "'";
  return update(sql);
}

// 创建表
export default {
  create: () => {
    return createTable(chatList);
  },
  drop: () => {
    return dropTable("chat_list");
  },
  /**
   * 查询该用户的所有聊天列表
   * @param {*} userId 
   * @returns 
   */
  getAll: () => {
    const sql = `select * from chat_list where id='${store.state.user.userInfo.id}'`;
    return selector(sql);
  },
  byToId: (toId) => {
    const sql = `select * from chat_list where id='${store.state.user.userInfo.id}' and toId = '${toId}'`;
    return selector(sql, true);
  },
  add: (obj) => addObj(obj),
  updateByToId,
  delByToId: (toId) => {
    console.log(toId);
    const sql = `delete from chat_list where id = '${store.state.user.userInfo.id}' and toId = '${toId}'`;
    return del(sql);
  }
}
