<template>
  <div>
    <p>封面:</p>
    <div class="thumb-div">
      <el-image
          v-if="newsItem.thumbUrl"
          style="width: 300px; max-height: 300px"
          :src="newsItem.thumbUrl"
          fit="contain"
      />

      <font-awesome-icon
          v-else
          class="avatar-uploader-icon"
          :class="isFirst ? 'avatar' : 'avatar1'"
          icon="fa-solid fa-plus"
      />

      <div class="thumb-but">
        <el-upload
            :action="UPLOAD_URL"
            :headers="HEADERS"
            multiple
            :limit="1"
            :file-list="fileList"
            :data="uploadData"
            :before-upload="onBeforeUpload"
            :on-error="onUploadError"
            :on-success="onUploadSuccess"
        >
          <template #trigger>
            <el-button size="small" type="primary">本地上传</el-button>
          </template>
          <el-button
              size="small"
              type="primary"
              @click="showImageDialog = true"
              style="margin-left: 5px"
          >
            素材库选择
          </el-button>
          <template #tip>
            <div class="el-upload__tip">支持 bmp/png/jpeg/jpg/gif 格式，大小不超过 2M</div>
          </template>
        </el-upload>
      </div>

      <el-dialog
          title="选择图片"
          v-model="showImageDialog"
          width="80%"
          append-to-body
          destroy-on-close
      >
        <WxMaterialSelect
            type="image"
            :account-id="accountId"
            @select-material="onMaterialSelected"
        />
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import WxMaterialSelect from '@/views/mp/components/wx-material-select/index.vue'
import { getAccessToken } from '@/utils/auth.js'
import { propTypes } from '@/utils/propTypes.js'
import { ref,reactive,inject,computed } from 'vue'
import ELComponent from '@/plugins/modal.js'
import { UploadType } from '@/utils/constants.js'

// 上传永久素材的地址
const UPLOAD_URL = import.meta.env.VITE_BASE_URL + '/admin-api/mp/material/upload-permanent'
// 设置上传的请求头部
const HEADERS = { Authorization: 'Bearer ' + getAccessToken() }

const props = defineProps({
  modelValue: propTypes.any,
  isFirst: propTypes.bool
})

const emit = defineEmits(["update:modelValue"])
const newsItem = computed({
  get() {
    return props.modelValue
  },
  set(val) {
    emit('update:modelValue', val)
  }
})

const accountId = inject('accountId')
const showImageDialog = ref(false)

const fileList = ref([])
const UploadData = {
  type: UploadType,
  accountId: propTypes.number
}
const uploadData = reactive({
  type: UploadType.Image,
  accountId: accountId
})

/** 素材选择完成事件*/
const onMaterialSelected = (item) => {
  showImageDialog.value = false
  newsItem.value.thumbMediaId = item.mediaId
  newsItem.value.thumbUrl = item.url
}

const onBeforeUpload = (rawFile) =>
    useBeforeUpload(UploadType.Image, 2)(rawFile)

const onUploadSuccess = (res) => {
  if (res.code !== 0) {
    message.error('上传出错：' + res.msg)
    return false
  }

  // 重置上传文件的表单
  fileList.value = []

  // 设置草稿的封面字段
  newsItem.value.thumbMediaId = res.data.mediaId
  newsItem.value.thumbUrl = res.data.url
}


const onUploadError = (err) => {
  message.error('上传失败: ' + err.message)
}

const useBeforeUpload = (type, maxSizeMB) => {
  const fn = (rawFile) => {
    let allowTypes = []
    let name = ''

    switch (type) {
      case UploadType.Image:
        allowTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/bmp', 'image/jpg']
        maxSizeMB = 2
        name = '图片'
        break
      case UploadType.Voice:
        allowTypes = ['audio/mp3', 'audio/mpeg', 'audio/wma', 'audio/wav', 'audio/amr']
        maxSizeMB = 2
        name = '语音'
        break
      case UploadType.Video:
        allowTypes = ['video/mp4']
        maxSizeMB = 10
        name = '视频'
        break
    }
    // 格式不正确
    if (!allowTypes.includes(rawFile.type)) {
      message.error(`上传${name}格式不对!`)
      return false
    }
    // 大小不正确
    if (rawFile.size / 1024 / 1024 > maxSizeMB) {
      message.error(`上传${name}大小不能超过${maxSizeMB}M!`)
      return false
    }

    return true
  }

  return fn
}
</script>

<style lang="scss" scoped>
.el-upload__tip {
  margin-left: 5px;
}

.thumb-div {
  display: inline-block;
  width: 100%;
  text-align: center;

  .avatar-uploader-icon {
    width: 120px;
    height: 120px;
    font-size: 28px;
    line-height: 120px;
    color: #8c939d;
    text-align: center;
    border: 1px solid #d9d9d9;
  }

  .avatar {
    width: 230px;
    height: 120px;
  }

  .avatar1 {
    width: 120px;
    height: 120px;
  }

  .thumb-but {
    margin: 5px;
  }
}
</style>
