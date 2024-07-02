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
            >
              <template #title>
                <div class="chapter-item">
                  <div
                      :class="[ isDelete ? 'text-red-600' : 'text-black',
                      opChapterIndex === index ? 'text-green-400' : '']"
                      :title="`${title},节有（${sectionList.length}）个，ID是：${id}`"
                      class="title"
                      @click="handleEditChapter(isDelete, index)"
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
                  <li
                      v-for="(lesson, cindex) in sectionList"
                      class="section-item"
                      @click="handleEditLessonIndex(index, cindex)"
                  >
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
        <el-form

            ref="formRef"
            v-loading="formChapterLoading"
            :hide-required-asterisk="true"
            :model="formData"
            :rules="rules"
            label-width="80px"
        >
          <div class="pug-chapter-box scrollbar">
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
                    <el-button v-if="!expandChapter" size="small" type="success" @click="expandChapter = true">展开</el-button>
                    <el-button v-else size="small" type="warning" @click="expandChapter = false">收起</el-button>
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
                      >添加章</el-button>
                      <el-button
                          v-if="courseForm.chapter.id && !courseForm.chapter.isDelete"
                          icon="edit"
                          size="default"
                          type="primary"
                          @click="handleSaveChapter"
                      >保存章</el-button>

                    </el-form-item>
                  </el-col>
                </el-row>
              </el-container>
            </div>

            <!--节编辑-->
            <div v-show="expandLesson" class="pug-section-form-box">
              <el-divider>
                <template #default>
                  <div class="divider-slot">
                    <el-icon><Reading /></el-icon>
                    <span class="section-form-title">节管理（{{ sectionList?.length }}节）</span>
                    <el-icon><Reading /></el-icon>
                  </div>
                </template>
              </el-divider>
              <div  class="py-5">
                <el-divider content-position="right">
                  <div class="px-2">
                    <el-button icon="Plus" size="small" type="primary" @click="handleAddSection">添加节</el-button>
                    <el-button
                        v-if="sectionActiveName.length === 0"
                        size="small"
                        type="success"
                        @click="sectionExpandAll">
                      展开
                    </el-button>
                    <el-button v-else size="small" type="warning" @click="sectionHideAll">收起</el-button>
                  </div>
                </el-divider>
              </div>

              <el-container style="width:100%;padding: 20px;background: #fff;">
                <el-collapse v-model="sectionActiveName" style="width:100%;" @change="handleChangeColl">
                  <el-collapse-item v-for="(lesson, index) in operationSectionList" :name="lesson.id">
                    <template #title>
                      <div class="chapter-item" style="width:100%">
                        <span
                            :class="[!lesson.isDelete ? 'text-black' : 'text-red-600',
                            lesson.id === -1 ? 'text-green-400' : '']">
                          第{{ index + 1 }}节：{{lesson.title}}
                        </span>
                        <div v-if="lesson.id !== -1">
                          <span v-if="lesson.isDelete" class="pr-5" title="删除" @click.stop="handleDel(lesson.id,opChapterIndex)"><el-icon><Delete /></el-icon></span>
                          <span v-if="!lesson.isDelete" class="pr-5" title="复制" @click.stop="handleCopy(lesson.id,opChapterIndex)"><el-icon><Notification /></el-icon></span>
                          <span v-if="!lesson.isDelete" class="pr-3" title="删除" @click="handleRemove(lesson.id,opChapterIndex)"><el-icon><CloseBold /></el-icon></span>
                          <span v-if="lesson.isDelete" class="pr-3" title="恢复" @click="handleReback(lesson.id,opChapterIndex)"><el-icon><CircleCheck /></el-icon></span>
                        </div>
                        <div v-else>
                          <span class="pr-5" title="删除" @click.stop="handleResetLesson"><el-icon><Delete /></el-icon></span>
                        </div>
                      </div>
                    </template>
                    <el-row :gutter="20" style="width:100%;padding:40px 0;">
                      <el-col :span="24">
                        <el-form-item label="节标题">
                          <el-input v-model="lesson.title" maxlength="100" placeholder="请输入章标题"></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="指派的课程" label-width="110">
                          <el-select
                              v-model="lesson.courseId"
                              disabled
                              placeholder="指派的课程"
                              @change="handleCourseChange(lesson.courseId,index)"
                          >
                            <el-option
                                v-for="item in courseListOption"
                                :key="item.id"
                                :label="item.title"
                                :value="item.id"
                            ></el-option>
                          </el-select>
                        </el-form-item>
                      </el-col>
                      <el-col :span="10">
                        <el-form-item label="指派章" label-width="70">
                          <el-select
                              v-model="lesson.pid"
                              placeholder="指派章"
                              style="width:100%"
                              @change="handleCategory"
                          >
                            <el-option
                                v-for="item in chapterListOption"
                                :key="item.id"
                                :label="item.title"
                                :value="item.id"
                            ></el-option>
                          </el-select>
                        </el-form-item>
                      </el-col>

                      <el-col :span="24">
                        <el-form-item label="节简介">
                          <el-input v-model="lesson.description" :rows="4" placeholder="请输入课程简介" type="textarea"></el-input>
                        </el-form-item>
                      </el-col>

                      <el-col :span="7">
                        <el-form-item label="是否免费">
                          <el-radio-group v-model="lesson.isFree">
                            <el-radio v-for="item in [{label:1,title:'是'},{label:0,title:'否'}]" :key="item.label" :label="item.label">
                              {{item.title}}
                            </el-radio>
                          </el-radio-group>
                        </el-form-item>
                      </el-col>

                      <el-col :span="7">
                        <el-form-item label="发布状态">
                          <el-radio-group v-model="lesson.status">
                            <el-radio
                                v-for="item in [{id:1, title:'是'}, {id:0,title:'否'}]"
                                :key="item.id"
                                :label="item.id"
                            >
                              {{item.title}}
                            </el-radio>
                          </el-radio-group>
                        </el-form-item>
                      </el-col>

                      <el-col :span="7">
                        <el-form-item label="是否删除">
                          <el-radio-group v-model="lesson.isDelete">
                            <el-radio v-for="item in [{label:1,title:'是'},{label:0,title:'否'}]" :key="item.label" :label="item.label">
                              {{item.title}}
                            </el-radio>
                          </el-radio-group>
                        </el-form-item>
                      </el-col>

                      <el-col :span="16">
                        <el-form-item label="播放码">
                          <el-input v-model="lesson.videoId" maxlength="100" placeholder="播放码"></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="8">
                        <el-form-item label="时长">
                          <el-input v-model="lesson.courseTimer" maxlength="100" placeholder="时长"></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="24">
                        <el-form-item
                            label='视频封面'
                            label-width='200px'
                            prop='cover'
                        >

                          <UploadImg
                              ref='imguplodRef'
                              v-model='lesson.videoFirstImage'
                              @success='handleUploadSuccess'
                          />
                        </el-form-item>

                        <el-form-item label='视频信息:' label-width='200px' prop='url'>

                          <div style='display: flex;justify-content: space-between'>
                            <el-input
                                v-model='lesson.videoLink'
                                placeholder='视频原始地址'
                                style='width: 35%'
                                title="视频原始地址(B)"
                            />
                            <el-input
                                v-model='lesson.courseTimer'
                                maxlength='20'
                                placeholder='原始大小'
                                style='width: 24%;margin: 0 1%;'
                                title='视频原始大小(B)'
                                type='number'
                            />
                            <el-button
                                class="mr5"
                                icon='Top'
                                type='primary'
                                @click='handleOpenUpload'
                            >
                              上传
                            </el-button>
                            <el-button
                                class="mr5"
                                icon='Delete'
                                type='danger'
                                @click='handleClearVideo'
                            >
                              清空
                            </el-button>
                          </div>
                        </el-form-item>
                        <el-form-item label='视频权重：' label-width='200px'>
                          <el-input
                              v-model='lesson.weight'
                              maxlength='10'
                              placeholder='视频权重'
                              style='width: 49.5%;margin-right: 1%;'
                              type='number'
                          />
                          <el-input
                              v-model='lesson.secretKey'
                              maxlength='100'
                              placeholder='视频秘钥'
                              style='width: 49.5%;'
                          />
                        </el-form-item>
                        <el-form-item label='标清视频名称：' label-width='200px' prop='stanUrl'>
                          <el-input v-model='lesson.videoLink' maxlength='200' placeholder='标清视频地址' style='width: 49.5%;margin-right: 1%;'></el-input>
                          <el-input v-model='lesson.courseTimer' maxlength='20' placeholder='标清大小' style='width: 49.5%'></el-input>
                        </el-form-item>
                        <el-form-item label='高清视频名称：' label-width='200px' prop='highUrl'>
                          <el-input
                              v-model='lesson.videoLink'
                              maxlength='200'
                              placeholder='高清视频地址'
                              style='width: 49.5%;margin-right: 1%;'
                          />
                          <el-input
                              v-model='lesson.courseTimer'
                              maxlength='20'
                              placeholder='高清大小'
                              style='width: 49.5%'
                              type='number'
                          />
                        </el-form-item>
                        <el-form-item label='超清视频名称：' label-width='200px' prop='superUrl'>
                          <el-input v-model='lesson.videoLink' maxlength='200' placeholder='超清视频地址'
                                    style='width: 49.5%;margin-right: 1%;'></el-input>
                          <el-input v-model='lesson.courseTimer' maxlength='20' placeholder='超清大小' style='width: 49.5%'></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="24" class="flex">
                        <el-form-item label="">
                          <el-button
                              v-if="lesson.id === -1"
                              icon="close"
                              size="default"
                              type="default"
                              @click.stop="handleResetLesson"
                          >
                            关闭
                          </el-button>
                          <el-button
                              v-if="lesson.id === -1"
                              icon="edit"
                              size="default"
                              type="primary"
                              @click.stop="handleSaveLesson(index)"
                          >
                            保存节
                          </el-button>
                          <el-button
                              v-if="lesson.id !== -1 && !lesson.isDelete"
                              icon="edit"
                              size="default"
                              type="primary"
                              @click.stop="handleSaveLesson(index)"
                          >
                            保存编辑节
                          </el-button>
                        </el-form-item>
                      </el-col>
                    </el-row>
                  </el-collapse-item>
                </el-collapse>

              </el-container>


            </div>
          </div>
        </el-form>
      </el-col>

    </el-row>

    <VideoUpload
        ref="videoUploadRef"
        @success='handleUploadVideoSuccess'
    />
  </Drawer>
</template>

<script setup>
import TagInputEcho from '@/components/TagInputEcho/index.vue'
import { ref, reactive, onMounted } from 'vue'
import { CommonStatusEnum } from '@/utils/constants'
import Drawer from '@/components/Drawer/index.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { findCourseList } from '@/api/course/online/course.js'
import { findSectionList, saveUpdateChapterLesson, getChapterLessons } from '@/api/course/online/chapter.js'
import ELComponent from '@/plugins/modal.js'
import UploadImg from '@/components/UploadImg/index.vue'
import VideoUpload from '@/components/VideoUpload/index.vue'

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
const formChapterLoading = ref(false)

// ============ 节属性 ============
// 控制节编辑(因为添加章的时候，因为还没有id，所以添加节是没有意义的，所以先隐藏)
const expandLesson = ref(false)
// 对应的节信息
const operationSectionList = ref([])
// 控制节的折叠菜单展开(这里需要的是展开的id)
const sectionActiveName = ref([1])
// 当前节的信息
const sectionIndex = ref(0)

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
  // 展开编辑章
  expandChapter.value = true
  // 收起编辑节
  expandLesson.value = false
}
/** 编辑章 */
const handleEditChapter = async (isDelete, index) => {
  formChapterLoading.value = true
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
  console.log('serverResponse.data.sectionList', serverResponse.data)
  // 节信息
  if(serverResponse.data.sectionList) {
    operationSectionList.value = serverResponse.data.sectionList
    console.log('编辑章的时候', operationSectionList.value)

  } else {
    operationSectionList.value = []
  }
  // 打开章编辑的状态
  expandChapter.value = true
  // 控制节编辑的状态
  expandLesson.value = true
  // 展开第一节进行编辑
  if(operationSectionList.value && operationSectionList.value.length > 0){
    sectionActiveName.value = [operationSectionList.value[0].id]
    // 如果想要展开所有
    // const lessonIds = lessonList.value.map(c=>c.id)
    // lessonActiveName.value = lessonIds
  }

  formChapterLoading.value = false
}
/** 添加标签 */
const handleAppendTags = (tagsList)=>{
  courseForm.chapter.tags = tagsList
}
const videoUploadRef = ref()
/** 视频上传 */
const handleOpenUpload = () => {
  videoUploadRef.value.expandUpload()
}
/** 视频分片上传的返回 */
const handleUploadVideoSuccess = (data) => {
  console.log("aaaaaaaaa",data)
  console.log("sectionIndex", sectionIndex.value)
  console.log("operationSectionList", operationSectionList.value)
  operationSectionList.value[sectionIndex.value].videoLink = data.url
  operationSectionList.value[sectionIndex.value].courseTimer = data.duration
  operationSectionList.value[sectionIndex.value].courseTimerSize = parseInt(data.duration)
  operationSectionList.value[sectionIndex.value].videoFirstImage = data.cover
}
// ====================== 节管理 ======================
/** 编辑节 -根据索引打开的方式 */
const handleEditLessonIndex = (index, cindex) => {
  // 记录编辑章的位置
  opChapterIndex.value = index
  // 打开编辑的节信息
  expandLesson.value = true
  // 展开编辑章
  expandChapter.value = false
  // 数据中的字段
  let chapter = chapterList.value[index]
  // 某个章信息
  courseForm.chapter = chapter
  // 通过 index 和 cindex 找到章下某个节
  const lesson = chapterList.value[index].sectionList[cindex]
  // 章下面的某个节信息
  operationSectionList.value = [lesson]
  // 展开编辑的节
  sectionActiveName.value = [lesson.id]
}
/** 新增节信息 */
const handleAddSection = () => {
  // 收起章
  expandChapter.value = false
  let len = operationSectionList.value.filter(lesson => lesson.id === -1).length
  // 如果有 > 0的id为-1那就是正在编辑的状态
  if(len > 0){
    return ELComponent.msgError("有正在编辑节，操作完毕在进行此操作...")
  }
  // 处于编辑状态
  courseForm.lesson.id = -1
  // 把当前添加的小节挂载到某个章下面
  courseForm.lesson.pid = courseForm.chapter.id

  // 编辑的时候新增的节管理先打开
  sectionActiveName.value = [-1]

  // 添加新节（每次添加新节的时候，我都会给id为-1）
  operationSectionList.value.push(courseForm.lesson)
}
/** 添加节 */
const handleSaveLesson = async (index) => {
  sectionIndex.value = index
  const lesson = operationSectionList.value[index]
  console.log('添加节的信息', lesson)
  // -1处于新增状态,一定要清空，否则就会就更新了就不对了。
  if(lesson.id === -1){
    lesson.id = ""
  }
  // 保存节的信息
  const serverResponse = await saveUpdateChapterLesson(lesson)
  // 转换成编辑的状态
  operationSectionList.value[index] = serverResponse.data
  ELComponent.msgSuccess('保存成功！')
  // 刷新
  await loadChapters(courseId.value)
}
/** 展开第一个节编辑 */
const sectionExpandAll = () => {

}
/** 关闭所有的节编辑 */
const sectionHideAll = () => {
  sectionActiveName.value = []
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
.pug-chapter-box.scrollbar {
  height: 93vh;
  overflow: scroll;
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

.pug-section-form-box .divider-slot .section-form-title {
  padding: 40px;
  font-size: x-large;
  font-weight: bold;
}
.pug-section-form-box /deep/ .el-divider__text {
  background: #f8f8f8;
}
</style>
