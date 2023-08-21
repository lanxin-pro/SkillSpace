<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
        ref="formRef"
        v-loading="formLoading"
        :model="formData"
        :rules="formRules"
        label-width="80px"
    >
      <el-form-item label="敏感词" prop="name">
        <template #label>
          <Tooltip
              message="如果是多个敏感词可以使用【,】来分割"
              titel="敏感词"
          />
        </template>
        <el-input v-model="formData.name" placeholder="请输入敏感词" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
              v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
              :key="dict.value"
              :label="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注" prop="description">
        <el-input v-model="formData.description" placeholder="请输入内容" />
      </el-form-item>
      <el-form-item label="标签" prop="tags">
        <el-select
            v-model="formData.tags"
            allow-create
            filterable
            multiple
            placeholder="请选择文章标签"
            style="width: 380px"
        >
          <el-option v-for="tag in tagList" :key="tag" :label="tag" :value="tag" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">确 定</el-button>
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
import {
  getSensitiveWord,
  createSensitiveWord,
  updateSensitiveWord,
  getSensitiveWordTagList,
  createBatchSensitiveWord
} from '@/api/system/sensitiveWord/index.js'
import Tooltip from '@/components/Tooltip/index.vue'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 弹窗的标题
const dialogTitle = ref('')
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('')
const formData = ref({
  id: undefined,
  name: '',
  status: CommonStatusEnum.ENABLE,
  description: '',
  tags: []
})
const formRules = reactive({
  name: [{ required: true, message: '敏感词不能为空', trigger: 'blur' }],
  tags: [{ required: true, message: '标签不能为空', trigger: 'blur' }]
})
// 表单 Ref
const formRef = ref()
// 标签数组
const tagList = ref([])

/** 打开弹窗 */
const open = async (type, id) => {
  dialogVisible.value = true
  dialogTitle.value = type
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const response = await getSensitiveWord(id)
      formData.value = response.data
    } finally {
      formLoading.value = false
    }
  }
  // 获得 Tag 标签列表
  const response = await getSensitiveWordTagList()
  tagList.value = response.data
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })

/** 提交表单 */
// 定义 success 事件，用于操作成功后的回调
const emit = defineEmits(['success'])
const submitForm = async () => {
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value
    if (formType.value === 'create') {
      console.log(data.name.split(","))
      if(data.name.includes("，")){
        ELComponent.msgWarning("请注意敏感词的大小写！")
        return
      }
      if(data.name.split(",").length > 1){
        await createBatchSensitiveWord(data)
        return
      }
      await createSensitiveWord(data)
      ELComponent.msgSuccess('添加成功！')
    } else {
      await updateSensitiveWord(data)
      ELComponent.msgSuccess('更新成功！')
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    name: '',
    status: CommonStatusEnum.ENABLE,
    description: '',
    tags: []
  }
  formRef.value?.resetFields()
}
</script>

<style scoped>

</style>
