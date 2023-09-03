<template>
  <Dialog v-model="dialogVisible" title="修改">
    <el-form
        ref="formRef"
        v-loading="formLoading"
        :model="formData"
        :rules="formRules"
        label-width="80px"
    >
      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="formData.nickname" placeholder="请输入昵称" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" placeholder="请输入备注" />
      </el-form-item>
      <el-form-item label="标签" prop="tagIds">
        <el-select v-model="formData.tagIds" clearable multiple placeholder="请选择标签">
          <el-option
              v-for="item in tagList"
              :key="item.tagId"
              :label="item.name"
              :value="item.tagId"
          />
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
import Dialog from '@/components/Dialog/index.vue'
import { ElTable } from 'element-plus'
import { ref,reactive } from 'vue'
import ElComponent from '@/plugins/modal.js'
import { getUser,updateUser } from '@/api/mp/mpuser/index.js'
import { getSimpleTagList } from '@/api/mp/tag/index.js'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 表单的加载中
const formLoading = ref(false)
const formData = ref({
  id: undefined,
  nickname: undefined,
  remark: undefined,
  tagIds: []
})
// 表单的校验
const formRules = reactive({})
// 表单 Ref
const formRef = ref()
// 公众号标签列表
const tagList = ref([])

/** 打开弹窗 */
const open = async (id) => {
  dialogVisible.value = true
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const response = await getUser(id)
      formData.value = response.data
    } finally {
      formLoading.value = false
    }
  }
  // 加载标签
  const response = await getSimpleTagList()
  tagList.value = response.data
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
    await updateUser(formData.value)
    ElComponent.msgSuccess("更新成功")
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
    nickname: undefined,
    remark: undefined,
    tagIds: []
  }
  formRef.value?.resetFields()
}
</script>

<style scoped>

</style>
