<template>
  <Dialog v-model="dialogVisible" title="用户导入" width="500">
    <el-upload
        ref="uploadRef"
        v-model:file-list="fileList"
        :action="importUrl + '?updateSupport=' + updateSupport"
        :auto-upload="false"
        :disabled="formLoading"
        :headers="uploadHeaders"
        :limit="1"
        :on-error="submitFormError"
        :on-exceed="handleExceed"
        :on-success="submitFormSuccess"
        accept=".xlsx, .xls"
        drag
    >
      <div class="uploader-context">
        <div>
          <font-awesome-icon icon="fa-solid fa-upload" />
        </div>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      </div>

      <template #tip>
        <div class="el-upload__tip text-center">
          <div class="el-upload__tip upload-text">
            <el-checkbox v-model="updateSupport" />
            是否更新已经存在的用户数据
          </div>
          <span>仅允许导入 xls、xlsx 格式文件。</span>
          <el-link
              :underline="false"
              style="font-size: 12px; vertical-align: baseline"
              type="primary"
              @click="importTemplate"
          >
            下载模板
          </el-link>
        </div>
      </template>
    </el-upload>
    <template #footer>
      <el-button :disabled="formLoading" size="small" type="primary" @click="submitForm">确 定</el-button>
      <el-button size="small" @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup>
import { ref,reactive } from 'vue'
import { CommonStatusEnum } from '@/utils/constants'
import Dialog from '@/components/Dialog/index.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 表单的加载中
const formLoading = ref(false)
const uploadRef = ref()
const importUrl =
    import.meta.env.VITE_BASE_URL + import.meta.env.VITE_API_URL + '/system/user/import'
// 上传 Header 头
const uploadHeaders = ref()
// 文件列表
const fileList = ref([])
// 是否更新已经存在的用户数据
const updateSupport = ref(0)


/** 打开弹窗 */
const open = () => {
  dialogVisible.value = true
  resetForm()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 重置表单 */
const resetForm = () => {
  // 重置上传状态和文件
  formLoading.value = false
  uploadRef.value?.clearFiles()
}

/** 提交表单 */
const submitForm = async () => {
  if (fileList.value.length === 0) {
    ELComponent.msgError('请上传文件')
    return
  }
  // 提交请求
  uploadRef.value.submit()
}

/** 上传错误提示 */
const submitFormError = () => {
  ELComponent.msgError('上传失败，请您重新上传！')
  formLoading.value = false
}

/** 文件数超出提示 */
const handleExceed = () => {
  ELComponent.msgError('最多只能上传一个文件！')
}

</script>

<style scoped>
.uploader-context {
  height: 150px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.upload-text {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 4px;
}
.el-upload__text {

}

</style>
