<template>
  <Drawer
      v-model="drawerVisible"
      :dialogEnableStatus="true"
      :withHeader="false"
      size="80%"
  >
    <el-row :gutter="20">
      <el-col :span="7">
        <div class="pug-chapter-box">
          <div>
            <el-button icon="plus" size="small" type="primary" @click="handleAddChapter">添加章</el-button>
          </div>
        </div>
      </el-col>
    </el-row>
  </Drawer>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { CommonStatusEnum } from '@/utils/constants'
import Drawer from '@/components/Drawer/index.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { findCourseList } from '@/api/course/online/chapter.js'
import { findChapterList } from '@/api/course/online/section.js'
import ELComponent from '@/plugins/modal.js'

// 控制展开与关闭
const drawerVisible = ref(false)
// 渲染课程下拉列表
const courseListOption = ref([])
// 当前编辑的课程id
const courseId = ref("")

const open = () => {
  drawerVisible.value = true

  // 查询课程列表
  loadCourseList()
}
// 关闭
const close = () => {
  drawerVisible.value = false
}
// 根据课程id查询章节信息
const loadChapters = async (cid) => {
  // 当前编辑的课程id
  courseId.value = cid
  // 根据课程查询对应的章信息
  const serverResponse = await findChapterList(cid)
  console.log('查询的课程节信息', serverResponse.data)
}
defineExpose({
  open,
  close,
  loadChapters
})
// 查询课程列表用户查询表单中章节的信息
const loadCourseList = async ()=>{
  const serverResponse = await findCourseList()
  courseListOption.value = serverResponse.data
}
</script>

<style scoped>
.pug-chapter-box {
  height: 94vh;
  background: #f8f8f8;
  padding: 10px;
}
</style>
