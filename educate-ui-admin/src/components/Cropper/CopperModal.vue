<template>
  <div>
    <Dialog
        v-model="dialogVisible"
        :canFullscreen="false"
        title="头像上传"
        maxHeight="380px"
        width="800px"
    >
      <div :class="prefixCls">
        <div :class="`${prefixCls}-left`">
          <div :class="`${prefixCls}-cropper`">
            <CropperImage
                v-if="src"
                :circled="circled"
                :src="src"
                height="300px"
                @cropend="handleCropend"
                @ready="handleReady"
            />
          </div>


          <!--    按钮      -->
          <div :class="`${prefixCls}-toolbar`">
            <el-upload
                :beforeUpload="handleBeforeUpload"
                :fileList="[]"
                accept="image/*"
            >
              <el-tooltip content="选择图片" placement="bottom">
                <el-button icon="Upload" type="primary" />
              </el-tooltip>
            </el-upload>

          </div>
        </div>

<!-- 右边预览图 -->
        <div :class="`${prefixCls}-right`">
          <div :class="`${prefixCls}-preview`">
            <img v-if="previewSource" alt="图片" :src="previewSource" />
          </div>
          <template v-if="previewSource">
            <div :class="`${prefixCls}-group`">
              <el-avatar :src="previewSource" size="large" />
              <el-avatar :size="48" :src="previewSource" />
              <el-avatar :size="64" :src="previewSource" />
              <el-avatar :size="80" :src="previewSource" />
            </div>
          </template>
        </div>
      </div>
      <template #footer>
        <el-button type="primary" @click="handleOk()">确认并上传</el-button>
      </template>
    </Dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import { propTypes } from '@/utils/propTypes'
import CropperImage from './Cropper.vue'
import { dataURLtoBlob } from '@/utils/filt.js'

const props = defineProps({
  // 图片的img
  srcValue: propTypes.string.def(''),
  circled: propTypes.bool.def(true)
})
// 自定义属性方法
const emit = defineEmits(['uploadSuccess'])
const src = ref(props.srcValue)
const prefixCls = 'v-cropper-am'
const cropper = ref()
const previewSource = ref('')
const dialogVisible = ref(false)
let filename = ''
let scaleX = 1
let scaleY = 1


const handleOk = async ()=>{
  const blob = dataURLtoBlob(previewSource.value)
  emit('uploadSuccess', { source: previewSource.value, data: blob, filename: filename })
}
const handleCropend = ({ imgBase64 })=>{
  previewSource.value = imgBase64
}
function handleReady(cropperInstance) {
  cropper.value = cropperInstance
}
// 图片上传的回调
const handleBeforeUpload = (file)=>{
  const reader = new FileReader()
  reader.readAsDataURL(file)
  src.value = ''
  previewSource.value = ''
  // 当文件读取完成时触发。在回调函数内部，将文件的data URL赋值给src.value，并将文件名赋值给filename变量
  reader.onload = function (e) {
    src.value = (e.target?.result) ?? ''
    filename = file.name
  }
  // 通过return false来取消默认的文件上传行为，以避免真正上传文件到服务器
  return false
}

// 开启dialog
function openModal() {
  dialogVisible.value = true
}
// 关闭dialog
function closeModal() {
  dialogVisible.value = false
}
// 导出方法
defineExpose({
  openModal,
  closeModal
})
</script>

<style lang="scss">
$prefix-cls: v-cropper-am;

.#{$prefix-cls} {
  display: flex;

  &-left,
  &-right {
    height: 340px;
  }

  &-left {
    width: 55%;
  }

  &-right {
    width: 45%;
  }

  &-cropper {
    height: 300px;
    background: #eee;
    background-image: linear-gradient(
            45deg,
            rgb(0 0 0 / 25%) 25%,
            transparent 0,
            transparent 75%,
            rgb(0 0 0 / 25%) 0
    ),
    linear-gradient(
            45deg,
            rgb(0 0 0 / 25%) 25%,
            transparent 0,
            transparent 75%,
            rgb(0 0 0 / 25%) 0
    );
    background-position: 0 0, 12px 12px;
    background-size: 24px 24px;
  }

  &-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 10px;
  }

  &-preview {
    width: 220px;
    height: 220px;
    margin: 0 auto;
    overflow: hidden;
    border: 1px solid;
    border-radius: 50%;

    img {
      width: 100%;
      height: 100%;
    }
  }

  &-group {
    display: flex;
    padding-top: 8px;
    margin-top: 8px;
    border-top: 1px solid;
    justify-content: space-around;
    align-items: center;
  }
}
</style>
