<template>
  <Dialog v-model="dialogVisible" title="测试发送邮件">
    <el-form
        ref="formRef"
        v-loading="formLoading"
        :model="formData"
        :rules="formRules"
    >

      <el-form-item label="收件邮箱" prop="mail">
        <el-input
            v-model="formData.mail"
            placeholder="请输入收件邮箱"
        />
      </el-form-item>
      <el-form-item
          v-for="param in formData.params"
          :key="param"
          :label="'参数 {' + param + '}'" :prop="'templateParams.' + param"
      >
        <el-input
            v-model="formData.templateParams[param]"
            :placeholder="'请输入 ' + param + ' 参数'"
        />
      </el-form-item>

      <el-form-item label="模板内容" prop="content">
        <Editor
            v-model="formData.content"
            :min-height="192"
            readonly
        />
      </el-form-item>

    </el-form>
    <template #footer>
      <el-button
          :disabled="formLoading"
          type="primary"
          @click="submitForm()"
      >
        确 定
      </el-button>
      <el-button
          @click="dialogVisible = false"
      >
        取 消
      </el-button>
    </template>
  </Dialog>
</template>

<script setup>
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import { DICT_TYPE, getIntDictOptions,getStrDictOptions } from '@/utils/dict.js'
import { sendMail,getMailTemplate } from '@/api/system/mail/template/index.js'
import { CommonStatusEnum, SystemMenuTypeEnum } from '@/utils/constants.js'
import ElComponent from '@/plugins/modal.js'
import Editor from '@/components/Editor/index.vue'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)

// 发送短信表单相关
const formData = ref({
  content: "",
  mail: "",
  templateParams: [],
  // 模板的参数列表
  params: [],
})
const formRules = reactive({
  mail: [{ required: true, message: "收件邮箱不能为空", trigger: "blur" }],
  templateCode: [{ required: true, message: "模版编码不能为空", trigger: "blur" }],
  templateParams: { }
})
// 表单 Ref
const formRef = ref()

const open = async (id) => {
  dialogVisible.value = true
  resetForm()
  // 设置数据
  formLoading.value = true
  try {
    const response = await getMailTemplate(id)
    // 设置动态表单
    formData.value.content = response.data.content
    formData.value.params = response.data.params
    formData.value.templateCode = response.data.code
    formData.value.templateParams = response.data.params.reduce((obj, item) => {
      // 给每个动态属性赋值，避免无法读取
      obj[item] = ''
      return obj
    }, {})
    formRules.templateParams = response.data.params.reduce((obj, item) => {
      obj[item] = { required: true, message: '参数 ' + item + ' 不能为空', trigger: 'blur' }
      return obj
    }, {})
  } finally {
    formLoading.value = false
  }
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
    const data = formData.value
    const response = await sendMail(data)
    const logId = response.data
    if (logId) {
      ElComponent.msgSuccess('提交发送成功！发送结果，见发送日志编号：' + logId)
    }
    dialogVisible.value = false
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    mail: '',
    params: {},
    content: '',
    templateParams: new Map()
  }
  formRef.value?.resetFields()
}
</script>

<style scoped>

</style>
