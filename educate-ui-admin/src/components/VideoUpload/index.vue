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
          @complete="complete"
          @file-added="onFileAdded"
          @file-complete="fileComplete"
          @file-success="onFileSuccess"
          @file-error="onFileError"
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
import { getAccessToken } from '@/utils/auth'
import ELComponent from '@/plugins/modal.js'
import SparkMD5 from 'spark-md5'
import { mergeChunks } from '@/api/video/file/index.js'

// 定义 success 事件，用于操作成功后的回调
const emit = defineEmits(['success'])

// 分片大小，20MB
const CHUNK_SIZE = 10 * 1024 * 1024
// 为了css样式的
const collapse = ref(true)

const uploaderPanelShow = ref(true)

const uploadFileList = ref([])

const options = {
  target: import.meta.env.VITE_VIDEO_UPLOAD_URL,
  headers: function (file, chunk, isTest) {
    debugger
    return {
      'fileIdentifier': file.uniqueIdentifier,
      'Authorization': getAccessToken()
    }
  },
  query: {
    'reqCloudType': 0,
    'isEncryption': 1,
    'isThumbnail': 0,
    'reupload': false,
    'remark': '业务后台-视频-新增'
  },
  withCredentials: true,
  // 是否开启服务器分片校验。默认为 true
  testChunks: true,
  // 真正上传的时候使用的 HTTP 方法,默认 POST
  uploadMethod: 'post',
  // 分片大小
  chunkSize: CHUNK_SIZE,
  // 并发上传数，默认为 3
  simultaneousUploads: 5,
  /**
   * 判断分片是否上传，秒传和断点续传基于此方法
   * 这里根据实际业务来 用来判断哪些片已经上传过了 不用再重复上传了 [这里可以用来写断点续传！！！]
   *
   * 暂停，刷新页面或在其它页面继续上传，续传功能主要还是依赖于 checkChunkUploadedByResponse 这个函数，其实每次重新上传前，
   * 都会执行文件校验，后端返回已接受到的文件片段位置，跳过这些已上传的文件片段达到续传的功能。
   */
  checkChunkUploadedByResponse: (chunk, message) => {
    console.log("aaaa message",message)
    options.query.reupload = false
    // message是后台返回
    const messageObj = JSON.parse(message)
    const dataObj = messageObj.data
    if (dataObj.skipUpload !== undefined) {
      return dataObj.skipUpload
    }
    // 判断文件或分片是否已上传，已上传返回 true
    // 这里的 uploaded 是后台返回]
    return (dataObj.uploaded || []).indexOf(chunk.offset + 1) >= 0
  },
  parseTimeRemaining: function (timeRemaining, parsedTimeRemaining) {
    // 格式化时间
    return parsedTimeRemaining
        .replace(/\syears?/, '年')
        .replace(/\days?/, '天')
        .replace(/\shours?/, '小时')
        .replace(/\sminutes?/, '分钟')
        .replace(/\sseconds?/, '秒')
  }
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
const fileSize = ref()
const onFileAdded = (file,fileList , event) => {
  uploadFileList.value.push(file)
  console.log('file :>> ', file)
  // 有时 fileType为空，需截取字符
  console.log('文件类型：' + file.fileType)
  // 文件大小
  console.log('文件大小：' + file.size + 'B')
  fileSize.value = file.size
  // 1. todo 判断文件类型是否允许上传
  // 2. 计算文件 MD5 并请求后台判断是否已上传，是则取消上传
  getFileMD5(file, (md5) => {
    if (md5 !== '') {
      // 修改文件唯一标识
      file.uniqueIdentifier = md5
      // 请求后台判断是否上传
      // 恢复上传
      file.resume()
    }
  })
}

const onFileSuccess = async (rootFile, file, response, chunk) => {
  console.log("上传成功的返回结果",response)
  if (!response) {
    ELComponent.msgWarning("上传失败！1")
    return
  }
  const result = JSON.parse(response)
  // TODO j-sentinel 我后端的统一结果返回 成功code 是 0
  if (result.code !== 0) {
    ELComponent.msgWarning("上传失败！2")
    return
  }
  let data = result.data || {}
  if (data) {
    ELComponent.msgSuccess("上传成功！3")
    await mergeChunks({
      identifier: file.uniqueIdentifier,
      filename: file.name,
      totalChunks: chunk.offset,
    })
    // data.filesize = fileSize.value || 0
    // 发送操作成功的事件
    emit('success',data)
  }
}
const onFileError = (rootFile, file, message, chunk) => {
  console.log('上传出错：' + message)
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


// 计算文件的MD5值
const getFileMD5 = (file, callback) => {
  const spark = new SparkMD5.ArrayBuffer()
  const fileReader = new FileReader()
  // 获取文件分片对象（注意它的兼容性，在不同浏览器的写法不同）
  const blobSlice =
      File.prototype.slice ||
      File.prototype.mozSlice ||
      File.prototype.webkitSlice
  // 当前分片下标
  let currentChunk = 0
  // 分片总数(向下取整)
  const chunks = Math.ceil(file.size / CHUNK_SIZE)
  // MD5加密开始时间
  const startTime = new Date().getTime()
  // 暂停上传
  file.pause()
  loadNext()
  // fileReader.readAsArrayBuffer操作会触发onload事件
  fileReader.onload = function (e) {
    // console.log("currentChunk :>> ", currentChunk);
    spark.append(e.target.result)
    if (currentChunk < chunks) {
      currentChunk++
      loadNext()
    } else {
      // 该文件的md5值
      const md5 = spark.end()
      console.log(
          `MD5计算完毕：${md5}，耗时：${new Date().getTime() - startTime} ms.`
      )
      // 回调传值md5
      callback(md5)
    }
  }
  fileReader.onerror = function () {
    ELComponent.msgError('文件读取错误')
    file.cancel()
  }
  // 加载下一个分片
  function loadNext() {
    const start = currentChunk * CHUNK_SIZE
    const end =
        start + CHUNK_SIZE >= file.size ? file.size : start + CHUNK_SIZE
    // 文件分片操作，读取下一分片(fileReader.readAsArrayBuffer操作会触发onload事件)
    fileReader.readAsArrayBuffer(blobSlice.call(file.file, start, end))
  }
}
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
