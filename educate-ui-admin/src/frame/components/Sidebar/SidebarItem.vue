<template>
  <div v-if="!item.hidden">
    <el-sub-menu ref="subMenu" :index="resolvePath(item.path)">
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
</script>

<style scoped>


</style>
