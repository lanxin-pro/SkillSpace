<template>
  <el-input
      type="textarea"
      :rows="5"
      placeholder="请输入内容"
      v-model="content"
  />
</template>

<script setup>
import { computed } from 'vue'
import { propTypes } from '@/utils/propTypes.js'

const props = defineProps({
  modelValue: propTypes.string.def("")
})
const emit = defineEmits(['update:modelValue','input'])

const content = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('update:modelValue', val)
    emit('input', val)
  }
})
</script>

<style scoped>

</style>
