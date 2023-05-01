
<template>
  <router-view class="v-grey-mode"></router-view>
</template>

<script setup>
import Logger from '@/utils/Logger'

Logger.prettyPrimary(`欢迎使用`, process.env.VUE_APP_TITLE)
</script>

<style lang="scss">
// 目前我需要撑满整个屏幕
.size {
  width: 100%;
  height: 100%;
}
html,
body {
  padding: 0 !important;
  margin: 0;
  // 隐藏容器中超出容器高度宽度的内容
  overflow: hidden;
  // 继承.size这个CSS类名的样式规则 .size这个类名可以在其他地方被定义。这个类名可能会包含很多属性声明
  @extend .size;
  #app {
    @extend .size;
  }
}

// 将输入图像完全转换为灰度图像。在该CSS类名被应用到元素上后，该元素内的所有内容都将变为灰色。
/*
.v-grey-mode {
  filter: grayscale(100%);
}
*/
</style>
