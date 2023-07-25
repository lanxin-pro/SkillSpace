<template>
  <el-card>
    <el-tabs v-model="activeName">
      <el-tab-pane label="基本信息" name="basicInfo">
        a
      </el-tab-pane>
      <el-tab-pane label="字段信息" name="colum">
        b
      </el-tab-pane>
      <el-tab-pane label="生成信息" name="generateInfo">
        c
      </el-tab-pane>
    </el-tabs>
    <el-form>
      <el-form-item style="float: right">
        <el-button :loading="formLoading" type="primary" @click="submitForm">保存</el-button>
        <el-button @click="close">返回</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { ref,reactive,computed,onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getCodegenTable } from '@/api/infra/codegen.js'


// 查询参数
const { query } = useRoute()
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const activeName = ref('colum') // Tag 激活的窗口
const basicInfoRef = ref()
const columInfoRef = ref()
const generateInfoRef = ref()
const formData = reactive({
  table: {},
  columns: []
})

/** 初始化 */
onMounted(() => {
  getDetail()
})

const getDetail = async ()=>{
  const id = query.id
  if (!id) {
    return
  }
  formLoading.value = true
  try {
    formData.value = await getCodegenTable(id)
  } finally {
    formLoading.value = false
  }
}
</script>

<style scoped>

</style>
