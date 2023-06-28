<template>
  <div>
      <font-awesome-icon
          :icon="isFullscreen? 'fa-solid fa-compress' : 'fa-solid fa-expand'"
          @click="click()"
      />
  </div>
</template>

<script setup>
import { ref,onMounted,onBeforeMount } from 'vue'
import screenfull from 'screenfull'

const isFullscreen = ref(false)


onMounted(()=>{
  init()
})
const init = ()=>{
  if (screenfull.isEnabled) {
    screenfull.on('change', change)
  }
}
onBeforeMount(()=>{
  destroy()
})
const destroy = ()=>{
  if (screenfull.isEnabled) {
    screenfull.off('change', change)
  }
}

const change = ()=>{
  isFullscreen.value = screenfull.isFullscreen
}
const click = ()=>{
  if (!screenfull.isEnabled) {
    this.$message({ message: '你的浏览器不支持全屏', type: 'warning' })
    return false
  }
  screenfull.toggle()
}
</script>

<style scoped>

</style>
