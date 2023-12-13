<script>
	export default {

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
