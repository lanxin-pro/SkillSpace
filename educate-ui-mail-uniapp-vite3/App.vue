<script>
export default {

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
