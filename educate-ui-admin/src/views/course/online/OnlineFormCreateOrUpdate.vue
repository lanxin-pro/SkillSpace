<template>
  <Dialog
      v-model="dialogVisible"
      :dialogEnableStatus="true"
      :lockScroll="true"
      :title="dialogTitle"
      maxHeight="700px"
      width="70%">

    <el-form
        ref="formRef"
        v-loading="formLoading"
        :model="formData"
        :rules="formRules"
        label-width="80px"
    >
      <fieldset class="course-from">
        <legend>基础信息</legend>
        <el-row>
          <el-col :span="15">
            <el-form-item label="课程标题" prop="title">
              <el-input v-model="formData.title" maxlength="30" minlength="1" placeholder="请输入课程标题"/>
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-form-item label="课程分类" prop="courseType">
              <el-select
                  v-model="formData.courseType"
                  placeholder="请选择课程状态"
                  style="width: 100%"
                  @change="handleCategory">
                <el-option
                    v-for="item in getIntDictOptions(DICT_TYPE.COURSE_AUTO_TYPE)"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <!--      课程封面        -->
          <el-col :span="24">
            <el-form-item label="课程封面" prop="img">
              <UploadImg v-model="formData.img" :limit="1" />
            </el-form-item>
          </el-col>
        </el-row>
      </fieldset>

      <fieldset class="course-from">
        <legend>价格设置</legend>
        <el-row :gutter="20" style="width:100%">
            <el-col :span="8">
              <el-form-item label="课程售价" prop="price">
                <el-input
                    v-model="formData.price"
                    :clearable="true"
                    placeholder="请输入课程售价"
                    prefix-icon="collection">
                  <template #append>元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="课程原价" prop="realPrice">
                <el-input
                    v-model="formData.realPrice"
                    :clearable="true"
                    placeholder="请输入课程原价"
                    prefix-icon="collection">
                  <template #append>元</template>
                </el-input>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="收藏数" prop="collects">
                <el-input v-model="formData.collects" disabled placeholder="请输入收藏数"></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="浏览数" prop="views">
                <el-input v-model="formData.views" disabled placeholder="请输入浏览数"></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="评论数" prop="comments">
                <el-input v-model="formData.comments" disabled placeholder="请输入评论数"></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="排序" prop="sorted">
                <el-input-number v-model="formData.sorted" :min="0" controls-position="right" placeholder="请输入序列" />
              </el-form-item>
            </el-col>
          </el-row>
      </fieldset>

      <fieldset class="course-from">
        <legend>状态控制</legend>
        <el-row :gutter="20" style="width:100%;">
          <el-col :span="8">
            <el-form-item label="课程发布状态" label-width="110px">
              <el-radio-group v-model="formData.status">
                <el-radio v-for="item in getIntDictOptions(DICT_TYPE.COURSE_PUBLISH_STATUS)" :key="item.value"
                          :label="item.value" disabled>
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="是否允许评论" label-width="110px" prop="isShow">
              <el-radio-group v-model="formData.comment">
                <el-radio
                    v-for="item in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
                    :key="item.value"
                    :label="item.value">
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="是否最新" prop="isNew">
              <el-radio-group v-model="formData.isNew">
                <el-radio
                    v-for="item in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
                    :key="item.value"
                    :label="item.value">
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否热门" prop="isHot">
              <el-select
                  v-model="formData.isHot"
                  placeholder="请选择课程是否推荐"
                  style="width: 100%">
                <el-option
                    v-for="item in getIntDictOptions(DICT_TYPE.COURSE_HOT)"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="是否推荐" prop="isPush">
              <el-select
                  v-model="formData.isPush"
                  placeholder="请选择课程是否推荐"
                  style="width: 100%">
                <el-option
                    v-for="item in getIntDictOptions(DICT_TYPE.COURSE_RECOMMEND)"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="是否置顶">
              <el-select
                  v-model="formData.goTop"
                  placeholder="请选择课程是否推荐"
                  style="width: 100%">
                <el-option
                    v-for="item in getIntDictOptions(DICT_TYPE.COURSE_TOP)"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>

        </el-row>
      </fieldset>

      <fieldset class="course-from">
        <legend>标签设置</legend>
        <el-row :gutter="20" style="width:100%;">
          <el-col :span="24">
            <el-form-item label="课程标签" prop="tags">
              <!--      这里的tags是我重新定义的一个响应式数据          -->
              <TagInputEcho
                  :tags-list="formData.tags"
                  @blur="handleAppendTags"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </fieldset>


      <fieldset class="course-from">
        <legend>课程详情&内容</legend>
        <el-row :gutter="20" style="width:100%">
          <el-col :span="24">
            <el-form-item label="课程简介" prop="description">
              <el-input
                  v-model="formData.description"
                  :rows="4"
                  placeholder="请输入课程简介"
                  type="textarea"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="课程详情" prop="htmlcontent">
              <Editor
                  v-model="formData.htmlContent"
                  :min-height="192"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </fieldset>


    </el-form>

    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>

  </Dialog>
</template>

<script setup>
import { ref,reactive } from 'vue'
import { CommonStatusEnum } from '@/utils/constants'
import Dialog from '@/components/Dialog/index.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import {
  createCourseOnline,
  updateCourseOnline,
  getCourseOnlineId
} from '@/api/course/online/chapter.js'
import { listSimplePosts } from '@/api/system/post.js'
import { listSimpleDepts } from '@/api/system/dept.js'
import { handleTree,defaultProps } from '@/utils/tree.js'
import ELComponent from '@/plugins/modal.js'
import Editor from '@/components/Editor/index.vue'
import UploadImg from '@/components/UploadImg/index.vue'
import TagInputEcho from '@/components/TagInputEcho/index.vue'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 弹窗的标题
const dialogTitle = ref('')
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)
// 表单的类型：create - 新增；update - 修改
const formType = ref('')
const formData = reactive({
  title: '', // 课程标题
  courseType: 1, // 分类
  img: '', // 图片地址
  comments: 0, // 评论人数
  sorted: 0, // 排序
  status: 0, // 发布状态 默认不发布
  collects: 0, // 收藏数
  views: 0, // 浏览数
  price: 0, // 价钱
  realPrice: 0, // 原价
  comment: 1, // 是否允许评论
  isNew: 1, // 是否最新
  isHot: 0, // 是否最热
  isPush: 0, // 是否推荐
  goTop: 0, // 是否指定
  tags: [], // 标签组
  description: "", // 课程简介
  htmlContent: "" // 课程详情
})
const tag = ref()
const tagsList = ref([])
// 表单 Ref
const formRef = ref()
/** 添加标签 */
const handleAppendTags = (tagsList)=>{
  formData.tags = tagsList
}
/** 删除标签 */
const handleDelTags = (index)=>{
  tagsList.value.splice(index,1)
}

/** 打开弹窗 */
const open = async (type, id) => {
  dialogVisible.value = true
  dialogTitle.value = type === 'create' ? '创建课程章信息' : '修改课程章信息'
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const response = await getCourseOnlineId(id)
      Object.assign(formData, response.data)
    } finally {
      formLoading.value = false
    }
  }
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  console.log('提交的数据', Object.assign(formData))
  // 校验表单
  if (!formRef){
    return
  }
  const valid = await formRef.value.validate()
  if (!valid){
    return
  }
  // 提交请求
  formLoading.value = true
  try {
    const data = Object.assign(formData)
    if (formType.value === 'create') {
      await createCourseOnline(data)
      ELComponent.msgSuccess("创建成功")
    } else {
      await updateCourseOnline(data)
      ELComponent.msgSuccess("更新成功")
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}
/** 重置表单 */
const resetForm = () => {
  Object.assign(formData, {
    title: '', // 课程标题
    courseType: 1, // 分类
    img: '', // 图片地址
    comments: 0, // 评论人数
    sorted: 0, // 排序
    status: 0, // 发布状态 默认不发布
    collects: 0, // 收藏数
    views: 0, // 浏览数
    price: 0, // 价钱
    realPrice: 0, // 原价
    comment: 1, // 是否允许评论
    isNew: 1, // 是否最新
    isHot: 0, // 是否最热
    isPush: 0, // 是否上传
    goTop: 0, // 是否置顶
    tags: [], // 标签组
    description: "", // 课程简介
    htmlContent: "" // 课程详情
  })
  formRef.value?.resetFields()
}
const formRules = reactive({
  title: [
    { required: true,message: '请输入课程标题', trigger: 'blur' },
    { min: 1, max: 30, message: '课程长度应该是1到30', trigger: 'blur' }
  ],
  img: [
    { required: true,message: '请选择图片', trigger: 'blur' },
  ],
  price: [
    { required: true,message: '请输入课程售价', trigger: 'blur' },
    { min: 1, max: 10000, message: '课程售价不合法', trigger: 'blur' }
  ],
  realPrice: [
    { required: true,message: '请输入课程原价', trigger: 'blur' },
    { min: 1, max: 10000, message: '课程原价不合法', trigger: 'blur' }
  ],
  description: [
    { required: true,message: '请输入课程简介', trigger: 'blur' },
    { min: 7,message: '课程简介字数不能少于7', trigger: 'blur' }
  ],
  htmlContent: [
    { required: true,message: '请输入课程详情', trigger: 'blur' },
    { min: 20, message: '课程详情字数不能少于20', trigger: 'blur' }
  ],
})

</script>

<style scoped>
.course-from {
  border: 1px solid #eee;
  padding: 30px 20px;
  margin-bottom: 20px;
}
.course-from:hover {
  background: #f8f8f8;
}
.course-from legend {
  padding: 0 20px;
  font-size: 16px;
  font-weight: 800;
}
.course-from:hover legend {
  color: #43ce5b;
  transition: all 0.6s ease;
}
</style>
