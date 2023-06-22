<template>
  <SvgIcon :icon-class="props.icon" />
  <span>{{props.title}}</span>
</template>

<script setup>
import SvgIcon from '@/components/SvgIcon/index.vue'
import { propTypes } from '@/utils/propTypes'
import { ref } from 'vue'

const props = defineProps({
  icon: propTypes.string.def(''),
  title: propTypes.string.def('')
})

const vnodes = ref([])

console.log(vnodes)

</script>

<style scoped>

</style>
