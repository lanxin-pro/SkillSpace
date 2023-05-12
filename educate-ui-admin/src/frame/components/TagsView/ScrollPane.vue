<template>
  <el-scrollbar ref="scrollContainer" :vertical="true" class="scroll-container" @wheel.native.prevent="handleScroll">
    <slot />
  </el-scrollbar>
</template>

<script setup>
const handleScroll = ()=>{

}
</script>

<style lang="scss" scoped>
.scroll-container {
  white-space: nowrap;
  position: relative;
  overflow: hidden;
  width: 100%;
  :deep(.el-scrollbar__bar) {
      bottom: 0;
    }
  :deep(.el-scrollbar__wrap) {
      height: 49px;
    }
}
</style>
