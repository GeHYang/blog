export const commentList1 = [
  {
    id: "1",// 评论id
    avatar: "../../static/headIcon.jpeg",
    nickName: "John Appleseed",
    content: "好看吗",
    createTime: "前天15:20",
    children: [
      {
        id: "2",
        pid: "1",// 父评论id
        avatar: "../../static/headIcon.jpeg",
        nickName: "John Appleseed",
        content: "好好看呀",
        createTime: "前天15:30",
      }
    ]
  }
];

export const commentList = {
  "1": [{
    id: "1",// 评论id
    avatar: "../../static/headIcon.jpeg",
    nickName: "John Appleseed",
    content: "好看吗",
    createTime: "前天15:20",
    children: [
      {
        id: "2",
        pid: "1",// 父评论id
        avatar: "../../static/4.jpeg",
        nickName: "张三",
        content: "好好看呀",
        createTime: "前天15:30",
      }
    ]
  }],
  "2": [{
    id: "3",// 评论id
    avatar: "../../static/4.jpeg",
    nickName: "张三",
    content: "看啥呢",
    createTime: "前天15:20",
    children: []
  }]
};