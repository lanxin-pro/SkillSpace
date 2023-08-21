<template>
  <Dialog v-model="dialogVisible" title="检测敏感词">
    <el-form
        ref="formRef"
        v-loading="formLoading"
        :model="formData"
        :rules="formRules"
        label-width="80px"
    >
      <el-form-item label="文本" prop="text">
        <el-input v-model="formData.text" placeholder="请输入测试文本" type="textarea" />
      </el-form-item>
      <el-form-item label="标签" prop="tags">
        <el-select
            v-model="formData.tags"
            allow-create
            filterable
            multiple
            placeholder="请选择标签"
            style="width: 380px"
        >
          <el-option v-for="tag in tagList" :key="tag" :label="tag" :value="tag" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">检 测</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import ELComponent from '@/plugins/modal.js'
import { CommonStatusEnum } from '@/utils/constants.js'
import { getSensitiveWordTagList,validateText } from '@/api/system/sensitiveWord/index.js'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)
const formData = ref({
  text: '',
  tags: []
})
const formRules = reactive({
  text: [{ required: true, message: '测试文本不能为空', trigger: 'blur' }],
  tags: [{ required: true, message: '标签不能为空', trigger: 'blur' }]
})
// 表单 Ref
const formRef = ref()
// 标签数组
const tagList = ref([])

/** 打开弹窗 */
const open = async () => {
  dialogVisible.value = true
  resetForm()
  // 获得 Tag 标签列表
  const response = await getSensitiveWordTagList()
  tagList.value = response.data
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })

/** 提交表单 */
const submitForm = async () => {
  // 校验表单
  if (!formRef){
    return
  }
  const valid = await formRef.value.validate()
  if (!valid){
    return
  }
  // 提交请求
  formLoading.value = true
  try {
    const form = formData.value
    const response = await validateText(form)
    if (response.data.length === 0) {
      ELComponent.msgSuccess('不包含敏感词！')
      return
    }
    ELComponent.msgWarning('包含敏感词：' + response.data.join(', '))
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    text: '',
    tags: []
  }
  formRef.value?.resetFields()
}
</script>

<style scoped>

</style>
