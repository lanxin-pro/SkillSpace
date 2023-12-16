<script>
	export default {
    globalData: {
      spid: 0,
      code: 0,
      isLogin: false,
      userInfo: {},
      MyMenus: [],
      windowHeight: 0,
      id: 0
    },
    // 初始化完成时触发（全局只触发一次）
    onLaunch: function(option) {
      let that = this
      // 获取导航高度；
      uni.getSystemInfo({
        success: function (res) {
          that.globalData.navHeight = res.statusBarHeight * (750 / res.windowWidth) + 91;
        }
      })
    }
	}
</script>

<style>
	/*每个页面公共css */
	@import url("@/plugin/animate/animate.min.css");
	@import 'static/css/base.css';
	@import 'static/css/oneself-widcss.css';
	@import 'static/iconfont/iconfont.css';
  @import 'static/css/guildford.css';

  // #ifdef H5
  body::-webkit-scrollbar,
  html::-webkit-scrollbar {
    display: none;
  }
  // #endif

  view {
    box-sizing: border-box;
  }

  .bg-color-red {
    background-color: #E93323 !important;
  }

  .syspadding {
    padding-top: var(--status-bar-height);
  }

  .flex {
    display: flex;
  }

  .uni-scroll-view::-webkit-scrollbar {
    /* 隐藏滚动条，但依旧具备可以滚动的功能 */
    display: none
  }

  ::-webkit-scrollbar {
    width: 0;
    height: 0;
    color: transparent;
  }
</style>
