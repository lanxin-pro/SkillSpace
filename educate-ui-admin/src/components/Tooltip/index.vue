<template>
  <span>{{ titel }}</span>
  <ElTooltip :content="message" placement="top">
    <font-awesome-icon style="color: #DB5F40" :icon="icon" />
  </ElTooltip>
</template>

<script setup>
import { propTypes } from '@/utils/propTypes'

defineProps({
  titel: propTypes.string.def(''),
  message: propTypes.string.def(''),
  icon: propTypes.string.def('fa-solid fa-question')
})
</script>

