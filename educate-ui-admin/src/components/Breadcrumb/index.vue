<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item,index) in levelList" :key="item.path">
        <span v-if="item.redirect==='noRedirect'||index===levelList.length-1" class="no-redirect">{{ item.meta.title }}</span>
        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script setup>
import { getCurrentInstance,onMounted,ref } from 'vue'
import router from "@/router/index.js"
const { appContext } = getCurrentInstance()


onMounted(()=>{
  getBreadcrumb()
})
const levelList = ref(null)

const getBreadcrumb = ()=>{
  // 只显示带有meta.title的路由
  let matched = appContext.config.globalProperties.$route.matched.filter(item => item.meta && item.meta.title)
  const first = matched[0]
  if (!isDashboard(first)) {
    matched = [{ path: '/index', meta: { title: '首页' }}].concat(matched)
  }
  levelList.value = matched.filter(item => item.meta && item.meta.title && item.meta.breadcrumb !== false)
}

const isDashboard = ((route)=>{
  const name = route && route.name
  if (!name) {
    return false
  }
  // return name.trim() === 'Index'
  return name.trim() === '首页' // 修复 Index 重复的问题
})
const handleLink = (item)=>{
  const { redirect, path } = item
  if (redirect) {
    router.push(redirect)
    return
  }
  router.push(path)
}
</script>

<style lang="scss" scoped>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;

  .no-redirect {
    color: #97a8be;
    cursor: text;
  }
}
</style>
