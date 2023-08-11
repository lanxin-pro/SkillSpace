<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
        ref="formRef"
        v-loading="formLoading"
        :model="formData"
        :rules="formRules"
        label-width="140px"
    >
      <el-form-item label="短信渠道编号" prop="channelId">
        <el-select v-model="formData.channelId" placeholder="请选择短信渠道编号">
          <el-option
              v-for="channel in channelList"
              :key="channel.id"
              :label="
              channel.signature +
              `【 ${getDictLabel(DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE, channel.code)}】`
            "
              :value="channel.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="短信类型" prop="type">
        <el-select v-model="formData.type" placeholder="请选择短信类型">
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_SMS_TEMPLATE_TYPE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="模板编号" prop="code">
        <el-input v-model="formData.code" placeholder="请输入模板编号" />
      </el-form-item>
      <el-form-item label="模板名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入模板名称" />
      </el-form-item>
      <el-form-item label="模板内容" prop="content">
        <el-input v-model="formData.content" placeholder="请输入模板内容" type="textarea" />
      </el-form-item>
      <el-form-item label="开启状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
              v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
              :key="dict.value"
              :label="parseInt(dict.value)"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="短信 API 模板编号" prop="apiTemplateId">
        <el-input v-model="formData.apiTemplateId" placeholder="请输入短信 API 的模板编号" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" placeholder="请输入备注" />
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
import { DICT_TYPE, getIntDictOptions,getStrDictOptions,getDictLabel } from '@/utils/dict.js'
import { getSmsTemplate,updateSmsTemplate,createSmsTemplate } from '@/api/system/sms/template/index.js'
import { getSimpleSmsChannelList } from '@/api/system/sms/channel/index.js'
import { getNotice,createNotice,updateNotice } from '@/api/system/notice/index.js'
import { CommonStatusEnum, SystemMenuTypeEnum } from '@/utils/constants.js'
import ElComponent from '@/plugins/modal.js'
import Editor from '@/components/Editor/index.vue'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 弹窗的标题
const dialogTitle = ref('')
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)
// 表单的类型
const formType = ref('')
const formData = ref({
  id: null,
  type: null,
  status: CommonStatusEnum.ENABLE,
  code: '',
  name: '',
  content: '',
  remark: '',
  apiTemplateId: '',
  channelId: null
})
const formRules = reactive({
  type: [{ required: true, message: '短信类型不能为空', trigger: 'change' }],
  status: [{ required: true, message: '开启状态不能为空', trigger: 'blur' }],
  code: [{ required: true, message: '模板编码不能为空', trigger: 'blur' }],
  name: [{ required: true, message: '模板名称不能为空', trigger: 'blur' }],
  content: [{ required: true, message: '模板内容不能为空', trigger: 'blur' }],
  apiTemplateId: [{ required: true, message: '短信 API 的模板编号不能为空', trigger: 'blur' }],
  channelId: [{ required: true, message: '短信渠道编号不能为空', trigger: 'change' }]
})
// 表单 Ref
const formRef = ref()
// 短信渠道列表
const channelList = ref([])

const open = async (type, id) => {
  dialogVisible.value = true
  dialogTitle.value = type
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const response = await getSmsTemplate(id)
      formData.value = response.data
    } finally {
      formLoading.value = false
    }
  }
  // 加载渠道列表
  const response = await getSimpleSmsChannelList()
  channelList.value = response.data
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
  formLoading.value = true
  try {
    const data = formData.value
    if (formType.value === 'create') {
      await createSmsTemplate(data)
      ElComponent.msgSuccess('创建成功！')
    } else {
      await updateSmsTemplate(data)
      ElComponent.msgSuccess('更新成功！')
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
    id: null,
    type: null,
    status: CommonStatusEnum.ENABLE,
    code: '',
    name: '',
    content: '',
    remark: '',
    apiTemplateId: '',
    channelId: null
  }
  formRef.value?.resetFields()
}
</script>

<style scoped>

</style>
