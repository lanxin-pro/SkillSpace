<template>
  <div
      :class="{'has-logo': showLogo}"
      :style="{ backgroundColor: settings.sideTheme === 'theme-dark' ?
      variables.menuBackground : variables.menuLightBackground }">
    <logo v-if="showLogo" :collapse="isCollapse()" />
    <el-scrollbar :class="settings.sideTheme" wrap-class="scrollbar-wrapper">
<!--
  default-active	页面加载时默认激活菜单的 index
  collapse	是否水平折叠收起菜单（仅在 mode 为 vertical 时可用）
  unique-opened	是否只保持一个子菜单的展开
  collapse-transition	是否开启折叠动画
  mode	菜单展示模式
-->
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
        <SidebarItem
            v-for="(route, index) in [{
  path: '/system/test',
  component: Layout,
  redirect: 'noRedirect',
  hidden: false,
  alwaysShow: true,
  meta: { title: '系统管理', icon : 'system' },
        children: [{
        path: 'index',
        component: null,
        name: 'Test',
        meta: {
        title: '测试管理',
        icon: 'user'
        }
        }]
        }]"
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
import variables from "@/assets/styles/variables.module.scss"
import store from '@/store'
import { usePermissionStore } from '@/piniastore/modules/permission.js'
import { computed, getCurrentInstance } from 'vue'

const { appContext } = getCurrentInstance()
const permissionStore = usePermissionStore()


console.log("==========>",variables)


const sidebarRouters = permissionStore.getRouters

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
  return true
})

const isCollapse = ()=>{
  return !sidebar.value.opened
}
</script>

<style scoped>

</style>
