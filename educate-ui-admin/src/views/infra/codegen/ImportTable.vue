<template>
  <Dialog v-model="dialogVisible" title="导入表" width="800px">

  </Dialog>
</template>

<script setup>
import Dialog from '@/components/Dialog/index.vue'
import { ElTable } from 'element-plus'
import { ref,reactive } from 'vue'
import ElComponent from '@/plugins/modal.js'

const dialogVisible = ref(false) // 弹窗的是否展示
const dbTableLoading = ref(true) // 数据源的加载中
const dbTableList = ref([]) // 表的列表
const queryParams = reactive({
  name: undefined,
  comment: undefined,
  dataSourceConfigId: 0
})
const queryFormRef = ref() // 搜索的表单
const dataSourceConfigList = ref([]) // 数据源列表


/** 打开弹窗 */
const open = async () => {
  dialogVisible.value = true
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗


</script>

<style scoped>

</style>
