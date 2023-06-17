<template>
  <ElIcon class="v-icon" :color="color" :size="size" >
    <span class="iconify" :data-icon="icon"></span>
  </ElIcon>
</template>

<script setup>
// 枚举类
import { propTypes } from '@/utils/propTypes.js'
import { computed,watch } from 'vue'

const props = defineProps({
  // icon name
  icon: propTypes.string,
  // icon color
  color: propTypes.string,
  // icon size
  size: propTypes.number.def(16),
  // icon svg
  svgClass: propTypes.string.def('')
})

watch(
    () => props.icon,
    (icon) => {
      console.log("监听"+icon)
    }
)
</script>

<style scoped>

</style>
