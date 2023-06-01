// 聊天信息表
import { createTable, dropTable } from './DDL';
import { selector,add, update, del } from './DML';
import store from '../store';
// 聊天信息表
const message = 
`create table if not exists message(
  id varchar(21) not null,
  toId varchar(21) not null,
  toUser varchar(2000) not null,
  content varchar(2000) null,
  type varchar(16) not null default 'text',
  isMe BOOLEAN not null,
  createTime DATETIME not null
);`;


// 创建表
export default {
  create: () => {
    return createTable(message);
  },
  drop: () => {
    return dropTable("message");
  },
  /**
   * 分页查询聊天列表
   * @param {*} toUserId 
   * @returns 
   */
  page: (toUserId, page = 0, size = 100) => {
    const sql = `select * from message where id='${store.state.user.userInfo.id}' and toId = '${toUserId}' 
    order by createTime desc limit ${(page - 1) * size}, ${size}`;
    return selector(sql);
  },
  add: (chatVo) => {
    // 构建数据
    let data = {...chatVo};
    delete data.unread;// 去掉unread
    data.toId = chatVo.toUser.id;
    data.id = store.state.user.userInfo.id;
    data.toUser = JSON.stringify(data.toUser);

    let sql = `insert into message values('${data.id}', '${data.toId}', '${data.toUser}',
    '${data.content}', '${data.type}', ${data.isMe ? 1 : 0}, '${data.createTime}')`;
    return add(sql);
  }
}