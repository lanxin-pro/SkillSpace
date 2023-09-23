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
     12312312312321
    </div>

  </div>

</template>

<script setup>
import { ref } from 'vue'

// 分片大小，20MB
const CHUNK_SIZE = 10 * 1024 * 1024

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
