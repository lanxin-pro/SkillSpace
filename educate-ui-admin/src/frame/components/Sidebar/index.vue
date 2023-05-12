<template>
  <div :class="{'has-logo':showLogo.value}" :style="{ backgroundColor: settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }">
    <logo v-if="showLogo.value" :collapse="isCollapse" />
    <el-scrollbar :class="settings.sideTheme" wrap-class="scrollbar-wrapper">
      <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :background-color="settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground"
          :text-color="settings.sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor"
          :unique-opened="true"
          :active-text-color="settings.theme"
          :collapse-transition="false"
          mode="vertical"
      >
        <!-- 根据 sidebarRouters 路由，生成菜单 -->
        <sidebar-item
            v-for="(route, index) in sidebarRouters"
            :key="route.path  + index"
            :item="route"
            :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
import Logo from "./Logo.vue"
import SidebarItem from "./SidebarItem.vue"
import variables from "@/assets/styles/variables.scss"
import store from '@/store'
import { computed,getCurrentInstance } from 'vue'

const { appContext } = getCurrentInstance()

const activeMenu = (()=>{
  const { meta, path } = appContext.config.globalProperties.$route;
  // if set path, the sidebar will highlight the path you set
  if (meta.activeMenu) {
    return meta.activeMenu;
  }
  return path;
})

const settings = computed(() => {
  return store.state.settings
})
const sidebar = computed(()=>{
  return store.getters["app/getSidebar"]
})
const showLogo = computed(()=>{
  return store.getters["settings/getSidebarLogo"]
})

const isCollapse = ()=>{
  return !sidebar.value.opened
}
</script>

<style scoped>

</style>
