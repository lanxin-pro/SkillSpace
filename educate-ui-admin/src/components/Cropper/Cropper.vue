<template>

    <img
        v-show="isReady"
        ref="imgElRef"
        :alt="alt"
        :crossorigin="crossorigin"
        :src="src"
        :style="getImageStyle"
    />

</template>

<script setup>
import { computed,ref,unref,onMounted,onUnmounted } from 'vue'
import Cropper from 'cropperjs'
import 'cropperjs/dist/cropper.css'
import { propTypes } from '@/utils/propTypes'
import { useDebounceFn } from '@vueuse/core'

const defaultOptions = {
  aspectRatio: 1,
  zoomable: true,
  zoomOnTouch: true,
  zoomOnWheel: true,
  cropBoxMovable: true,
  cropBoxResizable: true,
  toggleDragModeOnDblclick: true,
  autoCrop: true,
  background: true,
  highlight: true,
  center: true,
  responsive: true,
  restore: true,
  checkCrossOrigin: true,
  checkOrientation: true,
  scalable: true,
  modal: true,
  guides: true,
  movable: true,
  rotatable: true
}
const props = defineProps({
  src: propTypes.string.def(''),
  alt: propTypes.string.def(''),
  circled: propTypes.bool.def(false),
  realTimePreview: propTypes.bool.def(true),
  height: propTypes.string.def('360px'),
  crossorigin: {
    type: String,
    default: undefined
  },
  imageStyle: { type: Object,
    default: () => ({}) },
  options: { type: Object,
    default: () => ({}) }
})
const imgElRef = ref()
const cropper = ref()
const isReady = ref(false)
const emit = defineEmits(['cropend', 'ready', 'cropendError'])

const debounceRealTimeCroppered = useDebounceFn(realTimeCroppered, 80)

onMounted(()=>{
  init()
})

onUnmounted(() => {
  cropper.value?.destroy()
})
async function init() {
  const imgEl = unref(imgElRef)
  if (!imgEl) {
    return
  }
  cropper.value = new Cropper(imgEl, {
    ...defaultOptions,
    ready: () => {
      isReady.value = true
      realTimeCroppered()
      emit('ready', cropper.value)
    },
    crop() {
      debounceRealTimeCroppered()
    },
    zoom() {
      debounceRealTimeCroppered()
    },
    cropmove() {
      debounceRealTimeCroppered()
    },
    ...props.options
  })
}
// 只要框框发生改变/点击 每次都会去刷新
// 实时显示预览
function realTimeCroppered() {
  props.realTimePreview && croppered()
}

const getImageStyle = computed(()=> {
  return {
    height: props.height,
    maxWidth: '100%',
    ...props.imageStyle
  }
})
// 事件:返回base64和裁剪后的宽度和高度信息
// event: return base64 and width and height information after cropping
const croppered = ()=> {
  if (!cropper.value) {
    return
  }
  let imgInfo = cropper.value.getData()
  const canvas = props.circled ? getRoundedCanvas() : cropper.value.getCroppedCanvas()
  canvas.toBlob((blob) => {
    if (!blob) {
      return
    }
    let fileReader = new FileReader()
    fileReader.readAsDataURL(blob)
    fileReader.onloadend = (e) => {
      emit('cropend', {
        imgBase64: e.target?.result ?? '',
        imgInfo
      })
    }
    fileReader.onerror = () => {
      emit('cropendError')
    }
  }, 'image/png')
}
// 找一个圆形的画布
// Get a circular picture canvas
const getRoundedCanvas = () => {
  const sourceCanvas = cropper.value.getCroppedCanvas()
  const canvas = document.createElement('canvas')
  const context = canvas.getContext('2d')
  const width = sourceCanvas.width
  const height = sourceCanvas.height
  canvas.width = width
  canvas.height = height
  context.imageSmoothingEnabled = true
  context.drawImage(sourceCanvas, 0, 0, width, height)
  context.globalCompositeOperation = 'destination-in'
  context.beginPath()
  context.arc(width / 2, height / 2, Math.min(width, height) / 2, 0, 2 * Math.PI, true)
  context.fill()
  return canvas
}
</script>

<style scoped>

</style>
