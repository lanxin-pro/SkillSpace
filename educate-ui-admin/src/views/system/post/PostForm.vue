<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="600">
    <el-form
        ref="formRef"
        v-loading="formLoading"
        :model="formData"
        :rules="formRules"
        label-width="80px"
    >
      <el-form-item label="岗位标题" prop="name">
        <el-input v-model="formData.name" placeholder="请输入岗位标题" />
      </el-form-item>
      <el-form-item label="岗位编码" prop="code">
        <el-input v-model="formData.code" placeholder="请输入岗位编码" />
      </el-form-item>
      <el-form-item label="岗位顺序" prop="sort">
        <el-input v-model="formData.sort" placeholder="请输入岗位顺序" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="formData.status" clearable placeholder="请选择状态">
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" placeholder="请输备注" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm()">确 定</el-button>
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
import { getSimpleUserList, getSimpleUserSimpleDeptList } from '@/api/system/user.js'
import { getPost,createPost,updatePost } from '@/api/system/post.js'
import { handleTree,defaultProps } from '@/utils/tree.js'

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
  code: '',
  sort: 0,
  status: CommonStatusEnum.ENABLE,
  remark: ''
})
const formRules = reactive({
  name: [{ required: true, message: '岗位标题不能为空', trigger: 'blur' }],
  code: [{ required: true, message: '岗位编码不能为空', trigger: 'change' }],
  status: [{ required: true, message: '岗位状态不能为空', trigger: 'change' }],
  remark: [{ required: false, message: '岗位内容不能为空', trigger: 'blur' }]
})
// 表单 Ref
const formRef = ref()

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
      await createPost(data)
      ELComponent.msgSuccess(formData.value.name + '创建成功')
    } else {
      await updatePost(data)
      ELComponent.msgSuccess(formData.value.name + '更新成功')
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}


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
      const response = await getPost(id)
      formData.value = response.data
    } finally {
      formLoading.value = false
    }
  }
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })


/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    name: '',
    code: '',
    sort: undefined,
    status: CommonStatusEnum.ENABLE,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>
