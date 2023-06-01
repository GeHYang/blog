<style lang="scss">
/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
@import "uview-ui/index.scss";
@import url("@/static/font/project_font/iconfont.css");
</style>

<script>
export default {
  // APP启动时执行
  onLaunch: function () {
    this.$store.commit("LOAD_USERINFO");
    // 隐藏uni自带的默认底部导航栏TabBar
    uni.hideTabBar();
    this.initSqlite();
    this.$store.commit("CREATE_WS");
  },
  onShow: function () {},
  onHide: function () {
  },
  methods: {
    initSqlite(){
      // 开启sqlite连接
      if(!this.$sqlite.isOpen()){
        this.$sqlite.openDB();
        console.log("sqlite启动成功...")
      }
      // 创建信息表
      this.$sqlite.message.create();
    }
  },
};
</script>


<style>
/*每个页面公共css */
/* 具有底部导航栏页面的样式 */
.main {
  overflow: hidden;
  height: calc(100vh - 91px - var(--status-bar-height));
}
/* 不具有底部导航栏页面的样式 */
._main {
  height: calc(100vh - 45px - var(--status-bar-height));
}

.big-image {
  margin-top: calc(var(--status-bar-height) + 45px);
  padding: 0px 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
