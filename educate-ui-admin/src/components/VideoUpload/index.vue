<template>
  <div class="page-upload">
<!--
当我点击隐藏的时候，那么我全部的应用应该show掉，只为了保留头部信息，于是我就重新写了一个一模一样的
isExpandShow.value = false
isCollapseShow.value = true
    -->
    <Transition appear enter-active-class="animate__animated animate__fadeInDown">
      <div class="div-uploader-collapse" v-show="isCollapseShow">
        <div style="height:35px;background-color: white;text-align: right;">
          <span style="float:left;margin-left:5px;"><label>上传视频</label></span>
          <span style="float:right;">
              <el-icon style="font-size: 27px;cursor: pointer;margin-right: 10px;" @click="expandUpload"><Plus /></el-icon>
              <el-icon style="font-size: 28px;cursor: pointer" @click="closeUpload"><Close /></el-icon>
          </span>
        </div>
      </div>
    </Transition>

    <Transition appear enter-active-class="animate__animated animate__fadeInRight">
     <div class="div-uploader-expand" v-show="isExpandShow">

      <uploader
          :options="options"
          :file-status-text="statusText"
          class="uploader-example"
          ref="uploaderRef"
          @file-complete="fileComplete"
          @complete="complete"
      >
        <div style="height:35px;background-color: white;">
          <span style="float:left;font-size: 16px"><label>上传视频</label></span>
          <span style="float:right;">
            <el-icon style="font-size: 27px;cursor: pointer;margin-right: 10px;" @click="collapseUpload"><Minus /></el-icon>
            <el-icon style="font-size: 28px;cursor: pointer" @click="closeUpload"><Close /></el-icon>
          </span>
        </div>

        <uploader-unsupport></uploader-unsupport>
        <uploader-drop style="overflow:inherit;margin-bottom: 5px;border-radius: 6px">
          <p>拖动文件到这里上传</p>
          <uploader-btn single>选择文件</uploader-btn>
        </uploader-drop>

        <uploader-list>
          <div class="file-panel">
            <div class="file-title" v-show="uploaderPanelShow">
              <p class="file-list-title">文件列表</p>
              <div class="operate">
                <el-button
                    type="text"
                    @click="operate()"
                    :title="collapse ? '折叠' : '展开'"
                >

                  <el-icon v-if="collapse"><CaretBottom /></el-icon>
                  <el-icon v-else><CaretTop /></el-icon>
                </el-button>
                <el-button type="text" @click="close()" title="关闭">
                  <el-icon><Close /></el-icon>
                </el-button>
              </div>
            </div>
<!--     collapse false是折叠       -->
            <ul class="file-list" :class=" collapse ? 'uploader-list-ul-show' : 'uploader-list-ul-hidden'">
              <div class="no-file" v-if="!uploadFileList.length">
                <font-awesome-icon icon="fa-regular fa-folder-open" />
                暂无待上传文件
              </div>
            </ul>
          </div>
        </uploader-list>
      </uploader>
    </div>
    </Transition>

  </div>

</template>

<script setup>
import { ref } from 'vue'


// 分片大小，20MB
const CHUNK_SIZE = 10 * 1024 * 1024
// 为了css样式的
const collapse = ref(true)

const uploaderPanelShow = ref(true)

const uploadFileList = ref([])

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

/** 最大化上传窗口 */
const expandUpload = () => {
  isExpandShow.value = true
  isCollapseShow.value = false
}
/** 最小化上传窗口 */
const collapseUpload = () => {
  isExpandShow.value = false
  isCollapseShow.value = true
}
/** 关闭上传窗口 */
const closeUpload = () => {
  isExpandShow.value = false
  isCollapseShow.value = false
}

/** 折叠、展开面板动态切换 */
const operate = () => {
  collapse.value = collapse.value === false
}
/** 关闭折叠面板 */
const close = () => {
  uploaderPanelShow.value = false
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
/* 放置到右下角 */
.div-uploader-expand {
  position: fixed;
  right: 0;
  bottom: 0;
  z-index: 2999;
}
.uploader-example {
  width: 666px;
  padding: 15px;
  margin: 40px auto 0;
  font-size: 12px;
  background-color: white;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.4);
}
.file-panel {
  background-color: #fff;
  border: 1px solid #e2e2e2;
  border-radius: 7px 7px 0 0;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}
.file-title {
  display: flex;
  height: 60px;
  line-height: 60px;
  padding: 0 15px;
  border-bottom: 1px solid #ddd;
  background-color: #e7ecf2;
}
.file-list-title {
  font-size: 16px;
}
.operate {
  flex: 1;
  text-align: right;
}
/* 文件列表 */
.file-list {
  position: relative;
  height: 350px;
  overflow-x: hidden;
  overflow-y: auto;
  background-color: #fff;
  padding: 0;
  margin: 0 auto;
  transition: all 0.5s;
}

.no-file {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 16px;
}

.uploader-list-ul-hidden {
  height: 0;
}
</style>
