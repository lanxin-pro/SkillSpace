<template>
  <div v-loading="loading" :style="'height:' + height">

    <iframe
        ref="frameRef"
        :src="props.src"
        frameborder="no"
        scrolling="auto"
        style="width: 100%; height: 100%"
    ></iframe>

  </div>
</template>

<script setup>
import { ref,onMounted } from 'vue'
import { propTypes } from '@/utils/propTypes'

const props = defineProps({
  src: propTypes.string.def('')
})
const loading = ref(true)
const height = ref('')
const frameRef = ref(null)
const init = () => {
  height.value = document.documentElement.clientHeight - 94.5 + 'px'
  loading.value = false
}
onMounted(() => {
  setTimeout(() => {
    init()
  }, 300)
})
</script>

<style scoped>

</style>
