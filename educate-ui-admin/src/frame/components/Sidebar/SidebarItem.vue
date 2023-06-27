<template>
  <div v-if="!item.hidden">

    <template v-if="hasOneShowingChild(item.children,item)
     // 检查onlyOneChild.children是否不存在或者 noShowingChildren 为真 &&  item.alwaysShow是否为假
     && (!onlyOneChild.children || onlyOneChild.noShowingChildren) && !item.alwaysShow">

      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{'submenu-title-noDropdown':!isNest}">
          <item :icon="onlyOneChild.meta.icon||(item.meta&&item.meta.icon)" :title="onlyOneChild.meta.title" />
        </el-menu-item>
      </app-link>
    </template>


    <el-sub-menu v-else ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body>
      <template #title>
        <Item v-if="item.meta" :icon="item.meta && item.meta.icon" :title="item.meta.title" />
      </template>
<!--   前端的循环遍历   -->
      <SidebarItem
          v-for="(child, index) in item.children"
          :key="child.path + index"
          :is-nest="true"
          :item="child"
          :base-path="resolvePath(child.path)"
          class="nest-menu"
      />

    </el-sub-menu>
  </div>

</template>

<script setup>
import { isExternal } from '@/utils/validate'
import Item from './Item.vue'
import AppLink from './Link.vue'
import { propTypes } from '@/utils/propTypes'
import { ref } from 'vue'

const onlyOneChild = ref()

const props = defineProps({
  item: propTypes.object,
  basePath: propTypes.string.def(''),
  isNext: propTypes.bool.def(false)
})

/**
 * @children 子节点数组
 * @parent 父节点对象
 * @type {(function(*=, *): (boolean))|*}
 */
const hasOneShowingChild = ((children = [], parent)=>{
  // 如果子节点为空，那么就给定一个空数组
  if (!children) {
    children = [];
  }
  // 使用 filter 方法过滤子节点数组 children
  // 并返回一个新数组 showingChildren。
  // 在过滤的过程中，判断每个子节点对象 item 的 hidden 属性。
  // 如果 hidden 为真，表示该子节点被隐藏，将被过滤掉；否则，将其加入新数组 showingChildren。
  const showingChildren = children.filter(item => {
    // 是否隐藏 一般都是false
    if (item.hidden) {
      return false
    } else {
      // 临时设置(将在只有一个显示子节点时使用)
      onlyOneChild.value = item
      return true
    }
  })
  console.log("showingChildren",showingChildren)
  console.log("showingChildren.length",showingChildren.length)

  // 当只有一个子路由器时，默认显示该子路由器
  if (showingChildren.length === 1) {
    return true
  }

  // 如果没有要显示的子路由器，则显示父路由器
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ... parent, path: '', noShowingChildren: true }
    return true
  }

  return false
})

// 返回绝对路径
const resolvePath = ((routePath)=>{
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(props.basePath)) {
    return props.basePath
  }
  return generateRoutePath(props.basePath,routePath)
})

const generateRoutePath = (parentPath, path) => {
  if (parentPath.endsWith('/')) {
    parentPath = parentPath.slice(0, -1) // 移除默认的 /
  }
  if (!path.startsWith('/')) {
    path = '/' + path
  }
  return parentPath + path
}

</script>

<style scoped>


</style>
