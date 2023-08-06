<template>
  <div ref="elRef" :class="[$attrs.class]" :style="styles"></div>
</template>

<script setup>
import echarts from '@/plugins/echarts/index.js'
import { debounce } from 'lodash-es'
import 'echarts-wordcloud'
import { propTypes } from '@/utils/propTypes'
import { ref,computed,watch,unref,onActivated,onBeforeUnmount,onMounted } from 'vue'
import { isString } from '@/utils/is.js'

const props = defineProps({
  options: {
    type: Object,
    required: true
  },
  width: propTypes.oneOfType([Number, String]).def(''),
  height: propTypes.oneOfType([Number, String]).def('500px')
})

const theme = 'auto'

const options = computed(() => {
  return Object.assign(props.options, {
    darkMode: unref(theme)
  })
})

const elRef = ref()

let echartRef = null

const contentEl = ref()

const styles = computed(() => {
  const width = isString(props.width) ? props.width : `${props.width}px`
  const height = isString(props.height) ? props.height : `${props.height}px`

  return {
    width,
    height
  }
})

const initChart = () => {
  if (unref(elRef) && props.options) {
    echartRef = echarts.init(unref(elRef))
    echartRef?.setOption(unref(options))
  }
}

watch(
    () => options.value,
    (options) => {
      if (echartRef) {
        echartRef?.setOption(options)
      }
    },
    {
      deep: true
    }
)

const resizeHandler = debounce(() => {
  if (echartRef) {
    echartRef.resize()
  }
}, 100)

const contentResizeHandler = async (e) => {
  if (e.propertyName === 'width') {
    resizeHandler()
  }
}

onMounted(() => {
  initChart()

  window.addEventListener('resize', resizeHandler)

  contentEl.value = document.getElementsByClassName(`v-layout-content`)[0]
  unref(contentEl) &&
  (unref(contentEl)).addEventListener('transitionend', contentResizeHandler)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeHandler)
  unref(contentEl) &&
  (unref(contentEl)).removeEventListener('transitionend', contentResizeHandler)
})

onActivated(() => {
  if (echartRef) {
    echartRef.resize()
  }
})
</script>

<style scoped>

</style>
