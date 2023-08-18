<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="140px"
    >
      <el-form-item label="邮箱" prop="mail">
        <el-input
            v-model="formData.mail"
            placeholder="请输入邮箱"
        />
      </el-form-item>
      <el-form-item label="用户名" prop="username">
        <el-input
            v-model="formData.username"
            placeholder="请输入用户名，一般和邮箱一致"
        />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="formData.password" placeholder="请输入密码" />
      </el-form-item>
      <el-form-item label="SMTP 服务器域名" prop="host">
        <el-input v-model="formData.host" placeholder="请输入 SMTP 服务器域名" />
      </el-form-item>
      <el-form-item label="SMTP 服务器端口" prop="port">
        <el-input v-model="formData.port" placeholder="请输入 SMTP 服务器端口" />
      </el-form-item>
      <el-form-item label="是否开启 SSL" prop="sslEnable">
        <el-radio-group v-model="formData.sslEnable">
          <el-radio
              v-for="dict in getIntDictOptions(DICT_TYPE.INFRA_BOOLEAN_STRING)"
              :key="dict.label"
              :label="dict.label"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
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
import { getMailAccount,createMailAccount,updateMailAccount } from '@/api/system/mail/account/index.js'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 弹窗的标题
const dialogTitle = ref('')
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)
// 表单的类型：create - 新增；update - 修改
const formType = ref('')
const formData = ref({
  mail: undefined,
  username: '',
  password: '',
  sslEnable: CommonStatusEnum.ENABLE,
  host: '',
  port: ''
})
const formRules = reactive({
  mail: [{ required: true, message: "邮箱不能为空", trigger: "blur" }],
  username: [{ required: true, message: "用户名不能为空", trigger: "blur" }],
  password: [{ required: true, message: "密码不能为空", trigger: "blur" }],
  host: [{ required: true, message: "SMTP 服务器域名不能为空", trigger: "blur" }],
  port: [{ required: true, message: "SMTP 服务器端口不能为空", trigger: "blur" }],
  sslEnable: [{ required: true, message: "是否开启 SSL不能为空", trigger: "blur" }],
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
      const response = await getMailAccount(id)
      formData.value = response.data
    } finally {
      formLoading.value = false
    }
  }
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
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
      await createMailAccount(data)
      ELComponent.msgSuccess("创建成功！")
    } else {
      await updateMailAccount(data)
      ELComponent.msgSuccess("更新成功！")
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
    type: '',
    name: '',
    status: CommonStatusEnum.ENABLE,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>

<style scoped>

</style>
