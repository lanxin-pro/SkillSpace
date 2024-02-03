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
          <el-collapse v-model="chapterActiveName" :accordion="true">
            <el-collapse-item
                v-for="({deleted, id, sectionList, title},index) in chapterList"
                :class="sectionList.length === 0 ? 'hiddenSvg' : ''"
                :name="id"
            >
              <template #title>
                <div class="chapter-item">
                  <div
                      :class="[ deleted ? 'text-red-600' : 'text-black']"
                      :title="`${title},节有（${sectionList.length}）个，ID是：${id}`"
                      class="title"
                  >
                    <span>第{{ index + 1 }}章：</span>
                    <span>{{ title }}</span>
                  </div>
                  <div>
                    <span v-if="!deleted" class="pr-3" title="删除" @click.stop="handleDel(id, index)">
                      <el-icon><Delete /></el-icon>
                    </span>
                    <span v-if="deleted" class="pr-3" title="恢复" @click="handleReback(id, index)">
                      <el-icon><CircleCheck /></el-icon>
                    </span>
                    <span v-if="!deleted" class="pr-3" title="复制" @click.stop="handleCopy(id, index)">
                      <el-icon><Notification /></el-icon>
                    </span>
                  </div>
                </div>
              </template>
            </el-collapse-item>
          </el-collapse>
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
import { findSectionList } from '@/api/course/online/section.js'
import ELComponent from '@/plugins/modal.js'

// 控制展开与关闭
const drawerVisible = ref(false)
// 渲染课程下拉列表
const courseListOption = ref([])
// 当前编辑的课程id
const courseId = ref("")
// 控制章信息
const chapterList = ref([])
// 渲染课程下拉列表的章
const chapterListOption = ref([])

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
  const serverResponse = await findSectionList(cid)
  // 渲染页面
  chapterList.value = serverResponse.data
  // 用于节编辑章
  chapterListOption.value = serverResponse.data
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
.chapter-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 93%;
}
.chapter-item .title {
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 80%;
  overflow: hidden;
}
/* TODO j-sentinel 修改样式 */
.pug-chapter-box .hiddenSvg /deep/ .el-collapse-item__arrow {
  /* 在这里添加样式，可以覆盖element-plus中的样式 */
  display: none;
}
.hiddenSvg .chapter-item {
  width: 100%;
}

</style>
