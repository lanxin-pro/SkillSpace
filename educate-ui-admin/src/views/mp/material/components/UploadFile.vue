<template>
  <el-upload
      :action="UPLOAD_URL"
      :headers="HEADERS"
      multiple
      :limit="1"
      :file-list="fileList"
      :data="uploadData"
      :on-error="onUploadError"
      :before-upload="onBeforeUpload"
      :on-success="onUploadSuccess"
  >
    <el-button type="primary" plain> 点击上传 </el-button>
<!--  提示的tip 	提示说明文字  -->
    <template #tip>
      <span class="el-upload__tip" style="margin-left: 5px">
        <slot></slot>
      </span>
    </template>
  </el-upload>
</template>

<script setup>
import { propTypes } from '@/utils/propTypes.js'
import { ref, reactive, inject } from 'vue'
import ELComponent from '@/plugins/modal.js'
import {
  HEADERS,
  UPLOAD_URL,
  beforeImageUpload,
  beforeVoiceUpload
} from './upload.js'

const props = defineProps({
  accountId: propTypes.number
})

const fileList = ref([])
const emit = defineEmits(["uploaded"])
// tip全部的类型
const UploadType = reactive({
  Image: 'image',
  Voice: 'voice',
  Video: 'video'
})
const uploadData = reactive({
  accountId: -1,
  type: UploadType.Image,
  title: '',
  introduction: ''
})

/** 上传前检查 */
const onBeforeUpload = () => {
  uploadData.accountId = props.accountId
  return props.type === UploadType.Image ? beforeImageUpload : beforeVoiceUpload
}

/** 上传成功处理 */
const onUploadSuccess = (res) => {
  if (res.code !== 0) {
    ELComponent.alertError('上传出错：' + res.msg)
    return false
  }

  // 清空上传时的各种数据
  fileList.value = []
  uploadData.title = ''
  uploadData.introduction = ''

  ELComponent.notifySuccess('上传成功')
  emit('uploaded')
}

/** 上传失败处理 */
const onUploadError = (err) => {
  ELComponent.msgError('上传失败: ' + err.message)
}

</script>

<style lang="scss" scoped>
.el-upload__tip {
  margin-left: 5px;
}
</style>
