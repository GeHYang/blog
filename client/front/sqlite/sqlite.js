
const options = {
  name: 'blog',
  path: '_doc/blog.db'
}
/**
 * 执行sql语句
 * @param {*} sql 
 * @returns 
 */
export function executeSQL(sql){
	return new Promise((resolve, reject) => {
    plus.sqlite.executeSql({
      name: options.name,
      sql: sql,
      success: function(){
        resolve(true);
      },
      fail: function(e){
        console.error(e);
        reject(false);
      }
    });
  })
}
/**
 * 查询
 * @param {*} sql 
 */
export function select(sql, isOne){
	return new Promise((res, rej) => {
    plus.sqlite.selectSql({
      name: options.name,
      sql: sql,
      success: function(data){
        if(isOne && data.length > 0){
          res(data[0])
        } else
          res(data)
      },
      fail: function(e){
        console.error(e);
        rej(false);
      }
    });
  })
}
