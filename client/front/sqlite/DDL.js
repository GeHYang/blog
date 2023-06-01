import { executeSQL } from './sqlite';

/**
 * 创建聊天列表表
 * @returns 
 */
const createTable = (sql)=>{
  return executeSQL(sql);
}
/**
 * 删除表
 * @param {*} tableName 
 * @returns 
 */
const dropTable = (tableName) => {
  let sql = `drop table ${tableName}`;
  return executeSQL(sql);
}
export {
  createTable, dropTable
}
