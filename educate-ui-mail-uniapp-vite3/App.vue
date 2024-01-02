<script setup>
/* TODO j-sentine 这里适配H5端，目前实现后续优化 */
const onShow = () => {
  // #ifdef H5
  uni.getSystemInfo({
    success(e) {
      // TODO 芋艿：这样是否合理？？？
      /* 窗口宽度大于420px且不在PC页面且不在移动设备时跳转至 PC.html 页面 */
      if (e.windowWidth > 420 && !window.top.isPC && !/iOS|Android/i.test(e.system)) {
        // window.location.pathname = 'https://java.crmeb.net/';
        /* 若你的项目未设置根目录（默认为 / 时），则使用下方代码 */
        window.location.pathname = '/static/html/pc.html';
      }
    }
  })
  // #endif
}
</script>

<style lang="scss">
@import '@/sheep/scss/index.scss';
@import 'static/iconfont/iconfont.css';

page {
  font-size: 28rpx;
  background-color: #f5f5f5 !important;
  color: #333;
}

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
