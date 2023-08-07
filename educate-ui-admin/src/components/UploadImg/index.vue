<template>
  <div class="upload-box">
    <el-upload
        :action="updateUrl"
        :id="uuid"
        :class="['upload', drag ? 'no-border' : '']"
        :multiple="false"
        :show-file-list="false"
        :headers="uploadHeaders"
        :before-upload="beforeUpload"
        :on-success="uploadSuccess"
        :on-error="uploadError"
        :drag="drag"
        :accept="fileType.join(',')"
    >
      <template v-if="modelValue">
        <img :src="modelValue" class="upload-image" />
        <div class="upload-handle" @click.stop>
          <div class="handle-icon" @click="editImg">
            <font-awesome-icon icon="fa-solid fa-pen-nib" style="padding: 4px 0" />
            <span>修改</span>
          </div>
          <div class="handle-icon" @click="imgViewVisible = true">
            <font-awesome-icon icon="fa-brands fa-meta" style="padding: 4px 0" />
            <span>放大</span>
          </div>
          <div class="handle-icon" @click="deleteImg">
            <font-awesome-icon icon="fa-solid fa-trash-can" style="padding: 4px 0" />
            <span>删除</span>
          </div>
        </div>
      </template>
      <template v-else>
        <div class="upload-empty">
          <slot name="empty">
            <font-awesome-icon icon="fa-solid fa-plus" size="2xl" />
            <span>请上传图片</span>
          </slot>
        </div>
      </template>
    </el-upload>
    <div class="el-upload__tip">
      <slot name="tip"></slot>
    </div>
    <el-image-viewer
        v-if="imgViewVisible"
        @close="imgViewVisible = false"
        :url-list="[modelValue]"
    />
  </div>
</template>

<script setup>
import { generateUUID } from '@/utils/index.js'
import { propTypes } from '@/utils/propTypes'
import { getAccessToken, getTenantId } from '@/utils/auth'
import { ref } from 'vue'
import ELComponent from '@/plugins/modal.js'

// 接受父组件参数
const props = defineProps({
  modelValue: propTypes.string.def(''),
  updateUrl: propTypes.string.def(import.meta.env.VITE_UPLOAD_URL),
  // 是否支持拖拽上传 ==> 非必传（默认为 true）
  drag: propTypes.bool.def(true),
  // 是否禁用上传组件 ==> 非必传（默认为 false）
  disabled: propTypes.bool.def(false),
  // 图片大小限制 ==> 非必传（默认为 5M）
  fileSize: propTypes.number.def(5),
  // 图片类型限制 ==> 非必传（默认为 ["image/jpeg", "image/png", "image/gif"]）
  fileType: propTypes.array.def(['image/jpeg', 'image/png', 'image/gif']),
  // 组件高度 ==> 非必传（默认为 150px）
  height: propTypes.string.def('150px'),
  // 组件宽度 ==> 非必传（默认为 150px）
  width: propTypes.string.def('150px'),
  // 组件边框圆角 ==> 非必传（默认为 8px）
  borderRadius: propTypes.string.def('8px')
})

// 生成组件唯一id
const uuid = ref('id-' + generateUUID())
// 查看图片
const imgViewVisible = ref(false)

const emit = defineEmits(['update:modelValue'])

const deleteImg = () => {
  emit('update:modelValue', '')
}

const uploadHeaders = ref({
  Authorization: 'Bearer ' + getAccessToken()
  // TODO j-sentinel 这里的文件上传组件以后说不定需要写租户
})

const editImg = () => {
  const dom = document.querySelector(`#${uuid.value} .el-upload__input`)
  dom && dom.dispatchEvent(new MouseEvent('click'))
}

const beforeUpload = (rawFile) => {
  const imgSize = rawFile.size / 1024 / 1024 < props.fileSize
  const imgType = props.fileType
  if (!imgType.includes(rawFile.type))
  ELComponent.notifyWarning('上传图片不符合所需的格式！')
  if (!imgSize) ELComponent.notifyWarning(`上传图片大小不能超过 ${props.fileSize}M！`)
  return imgType.includes(rawFile.type) && imgSize
}

// 图片上传成功提示
const uploadSuccess = (res)=> {
  ELComponent.msgSuccess('图片上传成功！')
  emit('update:modelValue', res.data)
}

// 图片上传错误提示
const uploadError = () => {
  ELComponent.notifyError('图片上传失败，请您重新上传！')
}
</script>

<style scoped lang="scss">
.is-error {
  .upload {
    :deep(.el-upload),
    :deep(.el-upload-dragger) {
      border: 1px dashed var(--el-color-danger) !important;
      &:hover {
        border-color: var(--el-color-primary) !important;
      }
    }
  }
}
:deep(.disabled) {
  .el-upload,
  .el-upload-dragger {
    cursor: not-allowed !important;
    background: var(--el-disabled-bg-color);
    border: 1px dashed var(--el-border-color-darker) !important;
    &:hover {
      border: 1px dashed var(--el-border-color-darker) !important;
    }
  }
}
.upload-box {
  .no-border {
    :deep(.el-upload) {
      border: none !important;
    }
  }
  :deep(.upload) {
    .el-upload {
      position: relative;
      display: flex;
      align-items: center;
      justify-content: center;
      width: v-bind(width);
      height: v-bind(height);
      overflow: hidden;
      border: 1px dashed var(--el-border-color-darker);
      border-radius: v-bind(borderRadius);
      transition: var(--el-transition-duration-fast);
      &:hover {
        border-color: var(--el-color-primary);
        .upload-handle {
          opacity: 1;
        }
      }
      .el-upload-dragger {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 100%;
        height: 100%;
        padding: 0;
        overflow: hidden;
        background-color: transparent;
        border: 1px dashed var(--el-border-color-darker);
        border-radius: v-bind(borderRadius);
        &:hover {
          border: 1px dashed var(--el-color-primary);
        }
      }
      .el-upload-dragger.is-dragover {
        background-color: var(--el-color-primary-light-9);
        border: 2px dashed var(--el-color-primary) !important;
      }
      .upload-image {
        width: 100%;
        height: 100%;
        object-fit: contain;
      }
      .upload-empty {
        position: relative;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        font-size: 12px;
        line-height: 30px;
        color: var(--el-color-info);
        .el-icon {
          font-size: 28px;
          color: var(--el-text-color-secondary);
        }
      }
      .upload-handle {
        position: absolute;
        top: 0;
        right: 0;
        box-sizing: border-box;
        display: flex;
        align-items: center;
        justify-content: center;
        width: 100%;
        height: 100%;
        cursor: pointer;
        background: rgb(0 0 0 / 60%);
        opacity: 0;
        transition: var(--el-transition-duration-fast);
        .handle-icon {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          padding: 0 6%;
          color: aliceblue;
          .el-icon {
            margin-bottom: 40%;
            font-size: 130%;
            line-height: 130%;
          }
          span {
            font-size: 85%;
            line-height: 85%;
          }
        }
      }
    }
  }
  .el-upload__tip {
    line-height: 18px;
    text-align: center;
  }
}
</style>
