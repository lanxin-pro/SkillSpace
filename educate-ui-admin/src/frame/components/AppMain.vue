<template>
  <section class="app-main">
<!--  动画样式  -->
    <Transition appear enter-active-class="animate__animated animate__fadeInLeft">
      <keep-alive>
        <router-view />
      </keep-alive>
    </Transition>

    <iframe-toggle />
  </section>
</template>

<script setup>
import IframeToggle from "./IframeToggle/index.vue"
document.documentElement.style.setProperty('--animate-duration', '.5s');

</script>

<style lang="scss" scoped>


.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}

.fixed-header + .app-main {
  padding-top: 50px;
}

.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    min-height: calc(100vh - 84px);
  }

  .fixed-header + .app-main {
    padding-top: 84px;
  }
}

// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 17px;
  }
}
</style>
