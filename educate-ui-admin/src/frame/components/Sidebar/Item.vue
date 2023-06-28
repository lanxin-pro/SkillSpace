<template>
  <SvgIcon icon-color="red" icon-size="lg" :icon-class="props.icon" />
  <span>{{props.title}}</span>
</template>

<script setup>
import SvgIcon from '@/components/SvgIcon/index.vue'
import { propTypes } from '@/utils/propTypes'

const props = defineProps({
  icon: propTypes.string.def(''),
  title: propTypes.string.def('')
})

</script>

<style scoped>

</style>
