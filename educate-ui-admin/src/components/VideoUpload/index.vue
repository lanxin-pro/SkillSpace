<template>
  <div class="page-upload">

    <div class="div-uploader-collapse" v-show="isCollapseShow">
      <div style="height:35px;background-color: white;text-align: right;">
        <span style="float:left;margin-left:5px;"><label>上传视频</label></span>
        <span style="float:right;">
          <i style="font-size: 28px" class="el-icon-plus" @click="expandUpload"></i>
          <i style="font-size: 28px" class="el-icon-close" @click="closeUpload"></i>
        </span>
      </div>
    </div>

    <div class="div-uploader-expand" v-show="isExpandShow">

      <uploader
          :options="options"
          :file-status-text="statusText"
          class="uploader-example"
          ref="uploaderRef"
          @file-complete="fileComplete"
          @complete="complete"
      >
         <uploader-unsupport></uploader-unsupport>
        <uploader-drop>
          <p>Drop files here to upload or</p>
          <uploader-btn>select files</uploader-btn>
          <uploader-btn :attrs="attrs">select images</uploader-btn>
          <uploader-btn :directory="true">select folder</uploader-btn>
        </uploader-drop>
        <uploader-list></uploader-list>
      </uploader>
    </div>

  </div>

</template>

<script setup>
import { ref } from 'vue'


// 分片大小，20MB
const CHUNK_SIZE = 10 * 1024 * 1024

const options = {
  target: '//localhost:3000/upload', // '//jsonplaceholder.typicode.com/posts/',
  testChunks: false
}
const statusText = {
  success: 'success',
  error: 'error',
  uploading: 'uploading',
  paused: 'paused',
  waiting: 'waiting'
}
const complete = () => {
  console.log('complete', arguments)
}
const fileComplete = () => {
  console.log('file complete', arguments)
}

const isCollapseShow = ref(false)
const isExpandShow = ref(false)
const expandUpload = () => {
  isExpandShow.value = true
  isCollapseShow.value = false
}
// 提供 open 方法，用于打开弹窗
defineExpose({ expandUpload })
</script>

<style scoped>
.div-uploader-collapse {
  position: fixed;
  right: 0;
  bottom: 0;
  z-index: 2999;
  background-color: #fff;
  border: 1px solid #e2e2e2;
  border-radius: 7px 7px 0 0;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  padding: 5px 5px 5px 0;
  width: 666px;
}


</style>
