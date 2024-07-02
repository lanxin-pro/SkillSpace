<template>
  <Dialog v-model="dialogVisible" title="测试发送" :max-height="500">
    <el-form
        ref="formRef"
        v-loading="formLoading"
        :model="formData"
        :rules="formRules"
        label-width="140px"
    >
      <el-form-item label="模板内容" prop="content">
        <el-input
            v-model="formData.content"
            placeholder="请输入模板内容"
            readonly
            type="textarea"
        />
      </el-form-item>
      <el-form-item label="接收人" prop="userId">
        <el-select v-model="formData.userId" placeholder="请选择接收人">
          <el-option
              v-for="item in userOption"
              :key="item.id"
              :label="item.nickname"
              :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item
          v-for="param in formData.params"
          :key="param"
          :label="'参数 {' + param + '}'"
          :prop="'templateParams.' + param"
      >
        <el-input
            v-model="formData.templateParams[param]"
            :placeholder="'请输入 ' + param + ' 参数'"
        />
      </el-form-item>
    </el-form>
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
import { sendNotify,getNotifyTemplate } from '@/api/system/notify/template/index.js'
import { getSimpleUserList } from '@/api/system/user.js'
import ELComponent from '@/plugins/modal.js'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)
const formData = ref({
  content: '',
  params: {},
  userId: null,
  templateCode: '',
  templateParams: new Map()
})
const formRules = reactive({
  userId: [{ required: true, message: '用户编号不能为空', trigger: 'change' }],
  templateCode: [{ required: true, message: '模版编号不能为空', trigger: 'blur' }],
  templateParams: {}
})
// 表单 Ref
const formRef = ref()
const userOption = ref([])

const open = async (id) => {
  dialogVisible.value = true
  resetForm()
  // 设置数据
  formLoading.value = true
  try {
    const response = await getNotifyTemplate(id)
    console.log(response)
    // 设置动态表单
    formData.value.content = response.data.content
    formData.value.params = response.data.params
    formData.value.templateCode = response.data.code
    // obj是整体对象，item是数组元素的值
    formData.value.templateParams = response.data.params.reduce((obj, item) => {
      // 给每个动态属性赋值，避免无法读取，我就可以去创建
      obj[item] = ''
      return obj
    }, {})
    console.log("formData.value.templateParams",formData.value.templateParams)
    formRules.templateParams = response.data.params.reduce((obj, item) => {
      obj[item] = { required: true, message: '参数 ' + item + ' 不能为空', trigger: 'blur' }
      return obj
    }, {})
  } finally {
    formLoading.value = false
  }
  // 加载用户列表
  const response = await getSimpleUserList()
  userOption.value = response.data
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })

/** 提交表单 */
const submitForm = async () => {
  // 校验表单
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value
    const response = await sendNotify(data)
    if (response.data) {
      ELComponent.msgSuccess('提交发送成功！发送结果，见发送日志编号：' + response.data)
    }
    dialogVisible.value = false
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    content: '',
    params: {},
    mobile: '',
    templateCode: '',
    templateParams: new Map()
  }
  formRef.value?.resetFields()
}
</script>

<style scoped>

</style>
