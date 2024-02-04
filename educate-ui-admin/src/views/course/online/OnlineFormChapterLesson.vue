<template>
  <Drawer
      v-model="drawerVisible"
      :dialogEnableStatus="true"
      :withHeader="false"
      :lockScroll="true"
      size="80%"
  >
    <el-row :gutter="20">
      <el-col :span="7">
        <div class="pug-chapter-box">
          <div class="mb5">
            <el-button icon="plus" size="small" type="primary" @click="handleAddChapter">添加章</el-button>
          </div>
          <el-collapse v-model="chapterActiveName" :accordion="true">
            <el-collapse-item
                v-for="({isDelete, id, sectionList, title}, index) in chapterList"
                :class="sectionList.length === 0 ? 'hiddenSvg' : ''"
                :name="id"
                @click="handleEditChapter(isDelete, index)"
            >
              <template #title>
                <div class="chapter-item">
                  <div
                      :class="[ isDelete ? 'text-red-600' : 'text-black']"
                      :title="`${title},节有（${sectionList.length}）个，ID是：${id}`"
                      class="title"
                  >
                    <span>第{{ index + 1 }}章：</span>
                    <span>{{ title }}</span>
                  </div>
                  <div>
                    <span v-if="!isDelete" class="pr-3" title="删除" @click.stop="handleDel(id, index)">
                      <el-icon><Delete /></el-icon>
                    </span>
                    <span v-if="isDelete" class="pr-3" title="恢复" @click="handleReback(id, index)">
                      <el-icon><CircleCheck /></el-icon>
                    </span>
                    <span v-if="!isDelete" class="pr-3" title="复制" @click.stop="handleCopy(id, index)">
                      <el-icon><Notification /></el-icon>
                    </span>
                  </div>
                </div>
              </template>

              <div v-if="sectionList && sectionList.length > 0" class="pl-3">
                <ul class="section-item-list">
                  <li v-for="(lesson, cindex) in sectionList" class="section-item">
                    <a :class="[!lesson.isDelete ? 'text-black' : 'text-red-600']" href="javascript:void(0);">
                      <span>第{{ cindex + 1 }}节：</span>
                      <span>{{ lesson.title }}</span>
                    </a>
                    <div class="rightFunction">
                      <span v-if="!lesson.isDelete" class="pr-3" title="删除" @click="handleDel(lesson.id,index)">
                        <el-icon><Delete /></el-icon>
                      </span>
                      <span v-if="lesson.isDelete" class="pr-3" title="删除" @click="handleRemove(lesson.id,index)">
                        <el-icon><CloseBold /></el-icon>
                      </span>
                      <span v-if="lesson.isDelete" class="pr-3" title="恢复" @click="handleReback(lesson.id,index)">
                        <el-icon><CircleCheck /></el-icon>
                      </span>
                      <span v-if="!lesson.isDelete" class="pr-3" title="复制" @click="handleCopy(lesson.id,index)">
                        <el-icon><Notification /></el-icon>
                      </span>

                    </div>
                  </li>
                </ul>
              </div>
              <div v-else class="p-3">
                <div class="p-5 bg-light-400 text-center">暂无节信息</div>
              </div>
            </el-collapse-item>
          </el-collapse>
        </div>
      </el-col>


      <el-col :span="17">
        <div class="pug-chapter-box">
          <el-form
              ref="formRef"
              v-loading="formLoading"
              :hide-required-asterisk="true"
              :model="formData"
              :rules="rules"
              label-width="80px"
          >
            <!--章编辑-->
            <div class="pug-chapter-form-box">
              <el-divider>
                <template #default>
                  <div class="divider-slot">
                    <el-icon><Reading /></el-icon>
                    <span class="chapter-form-title">章管理</span>
                    <el-icon><Reading /></el-icon>
                  </div>
                </template>
              </el-divider>
              <div class="py-5">
                <el-divider content-position="right">
                  <div class="px-2">
                    <el-button v-if="!expandChapter" size="default" type="success" @click="expandChapter = true">展开</el-button>
                    <el-button v-else size="default" type="warning" @click="expandChapter = false">收起</el-button>
                  </div>
                </el-divider>
              </div>
              <el-container v-show="expandChapter" class="chapter-container">
                <el-row :gutter="20" style="width:100%">
                  <el-col :span="15">
                    <el-form-item label="章标题">
                      <el-input
                          v-model="courseForm.chapter.title"
                          maxlength="30"
                          placeholder="请输入章标题"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :span="9">
                    <el-form-item label="当前指派的课程" label-width="110">
                      <el-select
                          v-model="courseForm.chapter.courseId"
                          placeholder="指派的课程"
                      >
                        <el-option
                            v-for="(item, index) in courseListOption"
                            :key="item.id"
                            :label="item.title"
                            :value="item.id"
                        >{{index + 1}}.{{ item.title }}</el-option>
                      </el-select>
                      <el-button class="mt-2" icon="edit" size="small" type="primary" @click="loadChapters(courseForm.chapter.courseId)">跳转指派课程</el-button>
                    </el-form-item>
                  </el-col>

                  <el-col :span="24">
                    <el-form-item label="章简介">
                      <el-input v-model="courseForm.chapter.description" :rows="4" placeholder="请输入课程简介" type="textarea"></el-input>
                    </el-form-item>
                  </el-col>

                  <el-col :span="6">
                    <el-form-item label="发布状态">
                      <el-radio-group v-model="courseForm.chapter.status">
                        <el-radio v-for="item in [{id:1,title:'是'},{id:0,title:'否'}]"
                                  :key="item.id" :label="item.id">
                          {{item.title}}
                        </el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>

                  <el-col :span="6">
                    <el-form-item label="是否免费">
                      <el-radio-group v-model="courseForm.chapter.isFree">
                        <el-radio v-for="item in [{id:1,title:'是'},{id:0,title:'否'}]"
                                  :key="item.id" :label="item.id">
                          {{item.title}}
                        </el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>

                  <el-col :span="12">
                    <el-form-item label="章排序">
                      <el-input v-model="courseForm.chapter.sorted" maxlength="100" placeholder="章排序"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="时长">
                      <el-input v-model="courseForm.chapter.courseTimer" maxlength="100" placeholder="时长"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="标签">
                      <TagInputEcho :tags-list="courseForm.chapter.tags" @blur="handleAppendTags" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="24" class="flex">
                    <el-form-item label="">
                      <el-button
                          v-if="!courseForm.chapter.id"
                          icon="close"
                          size="default"
                          type="default"
                          @click="handleCancleSave"
                      >重置</el-button>
                      <el-button
                          v-if="!courseForm.chapter.id"
                          icon="edit"
                          size="default"
                          type="primary"
                          @click="handleSaveChapter"
                      >保存章</el-button>
                      <el-button
                          v-if="courseForm.chapter.id && !courseForm.chapter.isDelete"
                          icon="edit"
                          size="default"
                          type="primary"
                          @click="handleSaveChapter"
                      >编辑章</el-button>

                    </el-form-item>
                  </el-col>
                </el-row>
              </el-container>
            </div>
          </el-form>
        </div>
      </el-col>

    </el-row>
  </Drawer>
</template>

<script setup>
import TagInputEcho from '@/components/TagInputEcho/index.vue'
import { ref, reactive, onMounted } from 'vue'
import { CommonStatusEnum } from '@/utils/constants'
import Drawer from '@/components/Drawer/index.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { findCourseList } from '@/api/course/online/chapter.js'
import { findSectionList, saveUpdateChapterLesson, getChapterLessons } from '@/api/course/online/section.js'
import ELComponent from '@/plugins/modal.js'

// 控制展开与关闭
const drawerVisible = ref(false)
// 渲染课程下拉列表
const courseListOption = ref([])
// 当前编辑的课程id
const courseId = ref("")
// 控制章信息
const chapterList = ref([])
// 控制章的编辑的状态 收起/展开
const expandChapter = ref(true)
// 渲染课程下拉列表的章
const chapterListOption = ref([])
// 控制章的折叠菜单展开(这里需要的是展开的id)
const chapterActiveName = ref([])
// 记录章操作的索引
const opChapterIndex = ref(0)
/* 表单信息 */
// 综合数据模型
const mainObj = {
  id: "",
  title: "",
  content: "",
  tags: [],
  description: "",
  isDelete: 0,
  htmlContent: "",
  status: 1,
  pid: 0,
  courseId: courseId,
  sorted: 1,
  courseTimer: "",
  videoId: "",
  videoFirsTimage: "",
  videoMp4: "",
  videoLink: "",
  courseTimerSize: "",
  isFree: 1
}
// 控制章和节的响应式数据模型
// 数据 但是这里需要思考一个问题，如果我章和节用的是同一个，那么我这个在添加节的时候，我去赋值或者清空id就会出现很严重的问题
const  courseForm = reactive({
  chapter: { ...mainObj },
  lesson: { ...mainObj }
})

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
  reset('chapter')
  // 当前编辑的课程id
  courseId.value = cid
  // 根据课程查询对应的章信息
  const serverResponse = await findSectionList(cid)
  console.log('根据课程查询对应的章信息', serverResponse)
  // 渲染页面
  chapterList.value = serverResponse.data
  // 用于节编辑章
  chapterListOption.value = serverResponse.data

  if(chapterList.value && chapterList.value.length > 0){
    // 赋予章第一级展开
    // chapterActiveName.value = [chapterList.value[0].id]
    // 置顶展开的章节
    chapterActiveName.value = [chapterList.value[opChapterIndex.value].id]
    // 如果是展开所有
    // const chapterIds = chapterList.value.map(c=>c.id)
    // console.log(chapterActiveName.value)
  }
}
defineExpose({
  open,
  close,
  loadChapters
})
/** 查询课程列表用户查询表单中章节的信息 */
const loadCourseList = async ()=>{
  const serverResponse = await findCourseList()
  courseListOption.value = serverResponse.data
}

// ====================== 章管理 ======================
/** 保存章 */
const handleSaveChapter = async () => {
  try{
    console.log('添加章的信息', courseForm.chapter.courseId)
    const serverResponse = await saveUpdateChapterLesson(courseForm.chapter)
    console.log('添加章的信息 insert', serverResponse)
    // 保存之后，转换为编辑状态
    courseForm.chapter = serverResponse.data
    // 打开编辑的章
    expandChapter.value = true

    ELComponent.msgSuccess(courseForm.chapter.id ? "编辑成功" : "保存成功")
    await loadChapters(courseId.value)
  }catch (err){
    ELComponent.msgError(err.msg)
  }
}
/** 添加章 */
const handleAddChapter = ()=>{
  reset("chapter")
}
/** 编辑章 */
const handleEditChapter = async (isDelete, index) => {
  if(isDelete) {
    ELComponent.msgWarning('此章节已被删除')
    return;
  }
  // 记录编辑章的位置
  opChapterIndex.value = index
  const opid = chapterList.value[index].id
  const serverResponse = await getChapterLessons(opid)
  // 章信息
  courseForm.chapter = serverResponse.data
}
/** 添加标签 */
const handleAppendTags = (tagsList)=>{
  courseForm.chapter.tags = tagsList
}


/** 重置(可以重置章和节) */
const reset = (name)=>{
  courseForm[name] = {
    id: "",
    title: "",
    content: "",
    tags: [],
    description: "",
    isDelete: 0,
    htmlContent: "",
    status: 1,
    pid: 0,
    courseId: courseId,
    sorted: 1,
    courseTimer: "",
    videoId: "",
    videoFirsTimage: "",
    videoMp4: "",
    videoLink: "",
    courseTimerSize: "",
    isFree: 1
  }
}
</script>

<style scoped>
.pug-chapter-box {
  height: 94vh;
  background: #f8f8f8;
  padding: 10px 0;
  border-radius: 4px;
}
.chapter-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 93%;
  padding-left: 10px;
}
.chapter-item .title {
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 80%;
  overflow: hidden;
  font-weight: 600;
}
/* TODO j-sentinel 修改样式 */
.pug-chapter-box .hiddenSvg /deep/ .el-collapse-item__arrow {
  /* 在这里添加样式，可以覆盖element-plus中的样式 */
  display: none;
}
.hiddenSvg .chapter-item {
  width: 100%;
}
.section-item-list {

}
.section-item-list > .section-item {
  border-bottom: 1px solid #eee;
}
.section-item-list > .section-item a {
  transition: all 300ms;
}
.section-item .rightFunction {
  display: flex;
  justify-content: end;
}
.section-item-list > .section-item:last-child {
  border-bottom: none;
}
.section-item-list .section-item a:hover {
  color: #FF8D5D;
}
.section-item-list .section-item a {
  padding: 8px 5px 8px 3px;
  display: block;
}

.pug-chapter-form-box /deep/ .el-divider__text {
  background: #f8f8f8;
}
.pug-chapter-form-box .divider-slot {

}
.pug-chapter-form-box .divider-slot .chapter-form-title {
  padding: 40px;
  font-size: x-large;
  font-weight: bold;
}
.chapter-container {
  width:100%;
  padding: 30px 0;
  background:#fff;
}
</style>
