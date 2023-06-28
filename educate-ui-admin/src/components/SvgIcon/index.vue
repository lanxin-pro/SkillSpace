<template>
  <font-awesome-icon
      class="svg-icon"
      :icon="iconName"
      :size="iconSize"
  />
</template>

<script setup>
import { propTypes } from '@/utils/propTypes'
import { computed } from 'vue'

const props = defineProps({
  iconClass: propTypes.string,
  iconSize: propTypes.string,
  iconColor: propTypes.string.def('red')
})

const iconName = computed(()=>{
  return "fa-solid " + props.iconClass
})
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
