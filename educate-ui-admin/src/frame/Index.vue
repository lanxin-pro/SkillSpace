<template>
  <div :class="classObj" class="app-wrapper" :style="{'--current-color': theme}">
<!--  遮罩层  -->
    <div v-if="device === 'mobile' && sidebar.opened" class="drawer-bg" @click="handleClickOutside"/>

    <Sidebar v-if="!sidebar.hide" class="sidebar-container" />

    <div :class="{hasTagsView: needTagsView,sidebarHide: sidebar.hide}" class="main-container">
      <div :class="{'fixed-header': fixedHeader}">
        <Navbar/>
        <TagsView  v-if="needTagsView"/>
      </div>
      <AppMain/>


      <RightPanel>
        <Settings/>
      </RightPanel>
    </div>
  </div>
</template>

<script setup>
import RightPanel from '@/components/RightPanel/index.vue'
import { AppMain, Navbar, Settings, Sidebar, TagsView } from './components'
import store from '@/store'
import { computed } from 'vue'
import { ElNotification } from "element-plus"
import { formatAxis } from "@/utils/formatTime.js"

import { CACHE_KEY, useCache } from '@/hooks/web/useCache'
const { wsCache } = useCache()

if(!wsCache.get(CACHE_KEY.IS_LOGIN)){
  wsCache.set(CACHE_KEY.IS_LOGIN,true)
  ElNotification.success({
    title: formatAxis(new Date()),
    message: '欢迎您回来'
  })
}



const theme = store.getters['settings/getTheme']
const sideTheme = store.getters['settings/getSideTheme']
const sidebar = store.getters['app/getSidebar']
const device = store.getters['app/getDevice']
const needTagsView = store.getters['settings/getTagsView']
const fixedHeader = store.getters['settings/getFixeHeader']

const classObj = computed(() => {
  return {
    hideSidebar: !sidebar.opened,
    openSidebar: sidebar.opened,
    withoutAnimation: sidebar.withoutAnimation,
    mobile: device === 'mobile'
  }
})

const handleClickOutside = ()=>{
  console.log("点击了",handleClickOutside)
  // store.dispatch('app/closeSideBar',{ withoutAnimation: false })
}
</script>
<!-- 导入重复的组件定义 -->
<script>
import ResizeMixin from './mixin/ResizeHandler'

export default {
  name: 'Layout',
  mixins: [ResizeMixin]
}
</script>


<style lang="scss" scoped>
@import "@/assets/styles/mixin.scss";
@import "@/assets/styles/variables.module.scss";

.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  width: calc(100% - #{$base-sidebar-width});
  transition: width 0.28s;
}

.hideSidebar .fixed-header {
  width: calc(100% - 54px);
}

.sidebarHide .fixed-header {
  width: calc(100%);
}

.mobile .fixed-header {
  width: 100%;
}
</style>
