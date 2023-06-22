<template>
  <svg class="svg-icon" aria-hidden="true">
    <use xlink:href="#icon-user" />
  </svg>
</template>

<script setup>
import { isExternal } from '@/utils/validate.js'
import { propTypes } from '@/utils/propTypes'
import { computed } from 'vue'

const props = defineProps({
  iconClass: propTypes.string,
  className: propTypes.string
})

const isExternals = computed(()=>{
  return isExternal(props.iconClass)
})
const iconName = computed(()=>{
  return `#icon-${props.iconClass}`
})
const svgClass = computed(()=>{
  if (props.className) {
    return 'svg-icon ' + props.className
  } else {
    return 'svg-icon'
  }
})

const styleExternalIcon = computed(()=>{
  return {
    mask: `url(${props.iconClass}) no-repeat 50% 50%`,
    '-webkit-mask': `url(${props.iconClass}) no-repeat 50% 50%`
  }
})

console.log("isExternal:"+isExternals.value)
console.log(svgClass.value)
console.log(iconName.value)

</script>

<style scoped>
.svg-icon {
  width: 1em;
  height: 1em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}

.svg-external-icon {
  background-color: currentColor;
  mask-size: cover!important;
  display: inline-block;
}
</style>
