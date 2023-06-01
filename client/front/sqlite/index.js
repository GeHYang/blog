import chatList from './chatList';
import message from './message';

const options = {
  name: 'blog',
  path: '_doc/blog.db'
}

// 打开数据库
function openDB(){
	return new Promise((resolve, reject) => {
    plus.sqlite.openDatabase({
      name: options.name,
      path: options.path,
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
 * 是否关闭数据库
 * @returns 
 */
function isOpen() {
  return plus.sqlite.isOpenDatabase(options);
}

/**
 * 关闭数据库
 */
function closeDB(){
	return new Promise((resolve, reject) => {
    plus.sqlite.closeDatabase({
      name: options.name,
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

export default {
  openDB,isOpen,closeDB,
  chatList,
  message,
}