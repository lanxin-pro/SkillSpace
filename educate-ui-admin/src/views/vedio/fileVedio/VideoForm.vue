<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="1200">
    <el-form
        ref="formRef"
        v-loading="formLoading"
        :model="formData"
        :rules="formRules"
        label-width="80px"
    >
      <el-form-item label="公告标题" prop="title">
        <el-input v-model="formData.title" placeholder="请输入公告标题" />
      </el-form-item>
      <el-form-item label="公告类型" prop="type">
        <el-select v-model="formData.type" clearable placeholder="请选择公告类型">
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_NOTICE_TYPE)"
              :key="parseInt(dict.value)"
              :label="dict.label"
              :value="parseInt(dict.value)"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="formData.status" clearable placeholder="请选择状态">
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
              :key="parseInt(dict.value)"
              :label="dict.label"
              :value="parseInt(dict.value)"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" placeholder="请输备注" type="textarea" />
      </el-form-item>
    </el-form>
    <el-form-item label="公告内容" prop="content">
      <Editor v-model="formData.content" height="800px" />
    </el-form-item>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup>
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict.js'
import { getSimpleMenusList,getMenu,createMenu,updateMenu } from '@/api/system/menu/index.js'
import { getVideoAdmin } from '@/api/video/videoadmin/index.js'
import { CommonStatusEnum, SystemMenuTypeEnum } from '@/utils/constants.js'
import ElComponent from '@/plugins/modal.js'
import Editor from '@/components/Editor/index.vue'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 弹窗的标题
const dialogTitle = ref('')
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)
// 表单的类型：create - 新增；update - 修改
const formType = ref('')
const formData = ref({
  id: undefined,
  title: '',
  type: undefined,
  content: '',
  status: CommonStatusEnum.ENABLE,
  remark: ''
})
const formRules = reactive({
  title: [{ required: true, message: '公告标题不能为空', trigger: 'blur' }],
  type: [{ required: true, message: '公告类型不能为空', trigger: 'change' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'change' }],
  content: [{ required: true, message: '公告内容不能为空', trigger: 'blur' }]
})
// 表单 Ref
const formRef = ref()

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
      const response = await getVideoAdmin(id)
      console.log("视频详情",response.data)
      formData.value = response.data
    } finally {
      formLoading.value = false
    }
  }
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })

/** 提交表单 */
// 定义 success 事件，用于操作成功后的回调
const emit = defineEmits(['success'])
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
    const data = formData.value
    if (formType.value === 'create') {
      await createNotice(data)
      ElComponent.msgSuccess("创建成功")
    } else {
      await updateNotice(data)
      ElComponent.msgSuccess("更新成功")
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
    title: '',
    type: undefined,
    content: '',
    status: CommonStatusEnum.ENABLE,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>

<style scoped>

</style>
