<template>
  <div v-if="!item.hidden">

<!--  如果没有子类的话，就把侧边栏下拉隐藏  -->
    <template v-if="hasOneShowingChild(item.children,item) && (!onlyOneChild.children||onlyOneChild.noShowingChildren)&&!item.alwaysShow">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{'submenu-title-noDropdown':!isNest}">
          <item :icon="onlyOneChild.meta.icon||(item.meta&&item.meta.icon)" :title="onlyOneChild.meta.title" />
        </el-menu-item>
      </app-link>
    </template>


    <el-sub-menu v-else ref="subMenu" :index="resolvePath(item.path)">
      <template #title>
        <Item v-if="item.meta" :icon="item.meta && item.meta.icon" :title="item.meta.title" />
      </template>
<!--   前端的循环遍历   -->
      <SidebarItem
          v-for="(child, index) in item.children"
          :key="child.path + index"
          :is-nest="true"
          :item="child"
          class="nest-menu"
      />
    </el-sub-menu>
  </div>

</template>

<script setup>
import { isExternal } from '@/utils/validate'
import Item from './Item.vue'
import { propTypes } from '@/utils/propTypes'
import { ref } from 'vue'

const onlyOneChild = ref()

const props = defineProps({
  item: propTypes.object,
  basePath: propTypes.string
})


// 返回绝对路径
const resolvePath = ((routePath)=>{
  console.log("routePath"+routePath)
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(props.basePath)) {
    return props.basePath
  }
  return new URL(routePath, import.meta.url).pathname
})


const hasOneShowingChild = ((children = [], parent)=>{
  if (!children) {
    children = [];
  }
  const showingChildren = children.filter(item => {
    if (item.hidden) {
      return false
    } else {
      // Temp set(will be used if only has one showing child)
      onlyOneChild.value = item
      return true
    }
  })

  // When there is only one child router, the child router is displayed by default
  if (showingChildren.length === 1) {
    return true
  }

  // Show parent if there are no child router to display
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ... parent, path: '', noShowingChildren: true }
    return true
  }

  return false
})
</script>

<style scoped>


</style>
