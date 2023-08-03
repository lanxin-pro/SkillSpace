<template>
  <Dialog v-model="dialogVisible" title="上传文件" width="700">
    <el-upload
        ref="uploadRef"
        v-model:file-list="fileList"
        :action="url"
        :auto-upload="false"
        :data="data"
        :disabled="formLoading"
        :headers="uploadHeaders"
        :limit="1"
        :on-change="handleFileChange"
        :on-error="submitFormError"
        :on-exceed="handleExceed"
        :on-success="submitFormSuccess"
        accept=".jpg, .png, .gif"
        drag
    >
      <i class="el-icon-upload"></i>
      <div class="el-upload__text"> 将文件拖到此处，或 <em>点击上传</em></div>
      <template #tip>
        <div class="el-upload__tip" style="color: red">
          提示：仅允许导入 jpg、png、gif 格式文件！
        </div>
      </template>
    </el-upload>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitFileForm()">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup>
import { ref,reactive,unref } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import ELComponent from '@/plugins/modal.js'
import { getFilePage } from "@/api/infra/file/index.js"
import { getAccessToken } from '@/utils/auth'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 表单的加载中
const formLoading = ref(false)
const url = import.meta.env.VITE_UPLOAD_URL
// 上传 Header 头
const uploadHeaders = ref()
// 文件列表
const fileList = ref([])
const data = ref({ path: '' })
const uploadRef = ref()

/** 打开弹窗 */
const open = async () => {
  dialogVisible.value = true
  resetForm()
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })


/** 重置表单 */
const resetForm = () => {
  // 重置上传状态和文件
  formLoading.value = false
  uploadRef.value?.clearFiles()
}

/** 提交表单 */
const submitFileForm = () => {
  if (fileList.value.length == 0) {
    ELComponent.msgError('请上传文件')
    return
  }
  // 提交请求
  uploadHeaders.value = {
    // TODO j-sentinel 这里的令牌前缀需要优化
    Authorization: 'Bearer ' + getAccessToken()
    // TODO j-sentinel 这里以后或许需要上传租户id
  }
  unref(uploadRef)?.submit()
}

/** 处理上传的文件发生变化 */
const handleFileChange = (file) => {
  data.value.path = file.name
}

/** 上传错误提示 */
const submitFormError = () => {
  message.error('上传失败，请您重新上传！')
  formLoading.value = false
}

/** 文件数超出提示 */
const handleExceed = () => {
  message.error('最多只能上传一个文件！')
}

/** 文件上传成功处理 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitFormSuccess = () => {
  // 清理
  dialogVisible.value = false
  formLoading.value = false
  unref(uploadRef)?.clearFiles()
  // 提示成功，并刷新
  ELComponent.msgSuccess('上传成功')
  emit('success')
}

</script>

<style scoped>

</style>
