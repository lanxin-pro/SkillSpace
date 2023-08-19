<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="140px"
    >
      <el-form ref="form" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="模板名称" prop="name">
          <el-input
              v-model="formData.name"
              placeholder="请输入模板名称"
          />
        </el-form-item>
        <el-form-item label="模板编码" prop="code">
          <el-input
              v-model="formData.code"
              placeholder="请输入模板编码"
          />
        </el-form-item>
        <el-form-item label="邮箱账号" prop="accountId">
          <el-select
              v-model="formData.accountId"
              placeholder="请输入邮箱账号"
          >
            <el-option
                v-for="account in accountOptions"
                :key="account.id"
                :value="account.id"
                :label="account.mail"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="发送人名称" prop="nickname">
          <el-input
              v-model="formData.nickname"
              placeholder="请输入发送人名称"
          />
        </el-form-item>
        <el-form-item label="模板标题" prop="title">
          <el-input
              v-model="formData.title"
              placeholder="请输入模板标题"
          />
        </el-form-item>
        <el-form-item label="开启状态" prop="status">
          <el-radio-group
              v-model="formData.status"
          >
            <el-radio
                v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
                :key="dict.value"
                :label="parseInt(dict.value)"
            >
              {{dict.label}}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
              v-model="formData.remark"
              placeholder="请输入备注"
          />
        </el-form-item>
        <el-form-item label="模板内容">
          <Editor
              v-model="formData.content"
              :min-height="180"
          />
        </el-form-item>
      </el-form>
    </el-form>
    <template #footer>
      <el-button
          :disabled="formLoading"
          type="primary"
          @click="submitForm"
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
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import ELComponent from '@/plugins/modal.js'
import { CommonStatusEnum } from '@/utils/constants.js'
import { getMailTemplate,createMailTemplate,updateMailTemplate } from '@/api/system/mail/template/index.js'
import Editor from '@/components/Editor/index.vue'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 弹窗的标题
const dialogTitle = ref('')
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)
// 表单的类型：create - 新增；update - 修改
const formType = ref('')
const formData = ref({})
const formRules = reactive({
  name: [{ required: true, message: "模板名称不能为空", trigger: "blur" }],
  code: [{ required: true, message: "模板编码不能为空", trigger: "blur" }],
  accountId: [{ required: true, message: "邮箱账号不能为空", trigger: "blur" }],
  title: [{ required: true, message: "模板标题不能为空", trigger: "blur" }],
  content: [{ required: true, message: "模板内容不能为空", trigger: "blur" }],
  status: [{ required: true, message: "开启状态不能为空", trigger: "blur" }],
})
// 表单 Ref
const formRef = ref()
// 邮箱账号
const accountOptions = ref({})

/** 打开弹窗 */
const open = async (type, id, object) => {
  dialogVisible.value = true
  dialogTitle.value = type
  formType.value = type
  accountOptions.value = object
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const response = await getMailTemplate(id)
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
      await createMailTemplate(data)
      ELComponent.msgSuccess("创建成功！")
    } else {
      await updateMailTemplate(data)
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

  }
  formRef.value?.resetFields()
}

</script>

<style scoped>

</style>
