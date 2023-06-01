import { select, executeSQL } from './sqlite';
/**
 * 数据库操作接口
 */
// 1、根据id查询表信息
const byId = (id, tableName) => {
  // 构建sql语句
  const sql = `select * from ${tableName} where id = ${id}`;
  return select(sql, true);
}
/**
 * 插入新数据
 * @param {*} obj ：id、其他的键值对
 */
const add = async (sql) => {
  return executeSQL(sql);
}
/**
 * 根据id删除信息
 * @param {} id 
 * @param {*} tableName 
 * @returns 
 */
const del = (sql) => {
  return executeSQL(sql);
}
/**
 * 根据id更新用户信息
 * @param {*} id 
 * @param {*} obj 
 * @param {*} tableName 
 * @returns 
 */
const update = (sql) => {
  return executeSQL(sql);
}
/**
 * 查询所有
 * @param {*} tableName 
 * @returns 
 */
const getAll = (tableName) => {
  let sql = `select * from ${tableName}`;
  return select(sql);
}

const selector = (sql) => {
  return select(sql);
}

export {
  byId,
  add,
  del,
  update,
  getAll,
  selector
}