<template>
  <div v-if="!item.hidden">
    <el-sub-menu>
      <template #title>
        <Item v-if="item.meta" :icon="item.meta && item.meta.icon" :title="item.meta.title" />
      </template>
      <sidebar-item
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
import Item from './Item.vue'
import { propTypes } from '@/utils/propTypes'

const props = defineProps({
  item: propTypes.object
})

</script>

<style scoped>


</style>
