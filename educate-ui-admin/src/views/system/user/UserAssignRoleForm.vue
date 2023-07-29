<template>
  <Dialog v-model="dialogVisible" title="分配角色">


  </Dialog>
</template>

<script setup>
import { ref,reactive } from 'vue'
import { CommonStatusEnum } from '@/utils/constants'
import Dialog from '@/components/Dialog/index.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

// 弹窗的是否展示
const dialogVisible = ref(false)

/** 打开弹窗 */
const open = async (row) => {
  dialogVisible.value = true

}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

</script>

<style scoped>

</style>
