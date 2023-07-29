<template>
  <Dialog v-model="dialogVisible" title="用户导入" width="400">

  </Dialog>
</template>

<script setup>
import { ref,reactive } from 'vue'
import { CommonStatusEnum } from '@/utils/constants'
import Dialog from '@/components/Dialog/index.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 表单的加载中
const formLoading = ref(false)
const uploadRef = ref()
const importUrl =
    import.meta.env.VITE_BASE_URL + import.meta.env.VITE_API_URL + '/system/user/import'
// 上传 Header 头
const uploadHeaders = ref()
// 文件列表
const fileList = ref([])
// 是否更新已经存在的用户数据
const updateSupport = ref(0)


/** 打开弹窗 */
const open = () => {
  dialogVisible.value = true
  resetForm()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 重置表单 */
const resetForm = () => {
  // 重置上传状态和文件
  formLoading.value = false
  uploadRef.value?.clearFiles()
}

</script>

<style scoped>

</style>
