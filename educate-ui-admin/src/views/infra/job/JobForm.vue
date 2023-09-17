<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        v-loading="formLoading"
    >
      <el-form-item label="任务名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入任务名称" />
      </el-form-item>
      <el-form-item label="处理器的名字" prop="handlerName">
        <el-input
            :readonly="formData.id !== undefined"
            v-model="formData.handlerName"
            placeholder="请输入处理器的名字"
        />
      </el-form-item>
      <el-form-item label="处理器的参数" prop="handlerParam">
        <el-input v-model="formData.handlerParam" placeholder="请输入处理器的参数" />
      </el-form-item>
      <el-form-item label="CRON 表达式" prop="cronExpression">
        <Crontab v-model="formData.cronExpression" />
      </el-form-item>
      <el-form-item label="重试次数" prop="retryCount">
        <el-input
            v-model="formData.retryCount"
            placeholder="请输入重试次数。设置为 0 时，不进行重试"
        />
      </el-form-item>
      <el-form-item label="重试间隔" prop="retryInterval">
        <el-input
            v-model="formData.retryInterval"
            placeholder="请输入重试间隔，单位：毫秒。设置为 0 时，无需间隔"
        />
      </el-form-item>
      <el-form-item label="监控超时时间" prop="monitorTimeout">
        <el-input v-model="formData.monitorTimeout" placeholder="请输入监控超时时间，单位：毫秒" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button type="primary" @click="submitForm" :loading="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import { formatDate } from '@/utils/formatTime.js'
import ELComponent from '@/plugins/modal.js'
import { createJob,getJob,updateJob } from '@/api/infra/job/index.js'
import Crontab from '@/components/Crontab/index.vue'

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
  name: '',
  handlerName: '',
  handlerParam: '',
  cronExpression: ''
})
const formRules = reactive({
  name: [{ required: true, message: '任务名称不能为空', trigger: 'blur' }],
  handlerName: [{ required: true, message: '处理器的名字不能为空', trigger: 'blur' }],
  cronExpression: [{ required: true, message: 'CRON 表达式不能为空', trigger: 'blur' }],
  retryCount: [{ required: true, message: '重试次数不能为空', trigger: 'blur' }],
  retryInterval: [{ required: true, message: '重试间隔不能为空', trigger: 'blur' }]
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
      const response = await getJob(id)
      formData.value = response.data
    } finally {
      formLoading.value = false
    }
  }
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })

/** 提交按钮 */

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
      await createJob(data)
      ELComponent.success('添加成功！')
    } else {
      await updateJob(data)
      ELComponent.success('更新成功！')
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
    handlerName: '',
    handlerParam: '',
    cronExpression: ''
  }
  formRef.value?.resetFields()
}
</script>

<style scoped>

</style>
