<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="1200">
    <el-form
        ref="formRef"
        v-loading="formLoading"
        :model="formData"
        :rules="formRules"
        label-width="80px"
    >
      <div style='margin: 0 0 20px;padding:20px 120px 20px 20px;background: #fafafa;'>
        <h2 style='padding: 20px 0px 20px 120px'>基础信息</h2>
        <div style="display:none">
          <el-input v-model='formData.categoryId' type='hidden'></el-input>
          <el-input v-model='formData.categoryName' type='hidden'></el-input>
          <el-input v-model='formData.categoryPid' type='hidden'></el-input>
          <el-input v-model='formData.categoryPname' type='hidden'></el-input>
        </div>
        <el-form-item label='视频分类：' required label-width='200px'>
          <template v-if='selectCategoryList && selectCategoryList.length > 0'>
            <el-tag
                v-for='(tag,index) in selectCategoryList'
                @close='handleRemoveTag(index)'
                :key='index'
                closable
            >
              {{ tag.pname }} / {{ tag.name }}
            </el-tag>
          </template>
          <el-button
              @click='handleOpenCategory'
              type="primary"
              size='small'
              icon='Plus'
          >
            选择分类
          </el-button>
        </el-form-item>

        <el-form-item label='视频名称：' prop='title' required label-width='200px'>
          <el-input v-model='formData.title' placeholder='请输入视频名称，<=100字符' maxlength='100'></el-input>
        </el-form-item>
        <el-form-item label='视频介绍：' prop='intro' label-width='200px'>
          <el-input type='textarea' maxlength='400' placeholder='请输入附加参数 少于400字'
                    v-model='formData.intro'></el-input>
        </el-form-item>
        <el-form-item label='视频标签：' prop='intro' label-width='200px'>
          <div style='position: relative'>
            <div style='display: flex;justify-content: space-between'>
              <el-button
                  type='primary'
                  @click='handleOpenTagDialog'
                  size='small'
                  icon='Plus'
                  style='width: 176px;'
              >
                选择标签
              </el-button>
              <el-input
                  type='hidden'
                  maxlength='100'
                  v-model='formData.tagList'
                  readonly style='width: 100%;'></el-input>
            </div>
            <div>
              <el-tag
                  v-for='tag in tagArr'
                  :key='tag'
                  closable
                  @close='handleCloseTag(tag)'>
                {{ tag }}
              </el-tag>
              <el-input
                  class='input-new-tag'
                  v-model.trim='inputValue'
                  v-if='inputVisible'
                  ref='saveTagInput'
                  size='small'
                  @keyup.enter.native='handleInputConfirm'
                  @blur='handleInputConfirm'
              >
              </el-input>
              <el-button v-else class='button-new-tag' size='small' @click='showInput'>+ 新建标签</el-button>
            </div>
          </div>
        </el-form-item>
      </div>






    <VideoCategory ref="videoCategoryRef" @select='handleVideoSelectCategory'></VideoCategory>
    <VideoTag ref="videoTagRef" @select='handleVideoSelectTag'></VideoTag>





      <el-form-item label="公告标题" prop="title">
        <el-input v-model="formData.title" placeholder="请输入公告标题" />
      </el-form-item>
      <el-form-item label="公告类型" prop="type">
        <el-select v-model="formData.type" clearable placeholder="请选择公告类型">
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_NOTICE_TYPE)"
              :key="parseInt(dict.value)"
              :label="dict.label"
              :value="parseInt(dict.value)"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="formData.status" clearable placeholder="请选择状态">
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
              :key="parseInt(dict.value)"
              :label="dict.label"
              :value="parseInt(dict.value)"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" placeholder="请输备注" type="textarea" />
      </el-form-item>
    </el-form>
    <el-form-item label="公告内容" prop="content">
      <Editor v-model="formData.content" height="800px" />
    </el-form-item>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup>
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict.js'
import { getSimpleMenusList,getMenu,createMenu,updateMenu } from '@/api/system/menu/index.js'
import { getVideoAdmin } from '@/api/video/videoadmin/index.js'
import { CommonStatusEnum, SystemMenuTypeEnum } from '@/utils/constants.js'
import ElComponent from '@/plugins/modal.js'
import Editor from '@/components/Editor/index.vue'
import VideoCategory from './components/VideoCategory.vue'
import VideoTag from './components/VideoTag.vue'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 弹窗的标题
const dialogTitle = ref('')
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)
// 表单的类型：create - 新增；update - 修改
const formType = ref('')

// 表单选择分类数组
const selectCategoryList = ref([])

const formData = ref({
// 标题：须限定字数200
  title: '',
  // 简介：须限定字数500
  intro: '',
  // 视频地址
  url: '',
  // 视频大小
  size: 0,
  // 标清视频地址
  stanUrl: '',
  // 高清视频地址
  highUrl: '',
  // 超清视频地址
  superUrl: '',
  // 标清视频大小
  stanSize: 0,
  // 高清视频大小
  highSize: 0,
  // 超清视频大小
  superSize: 0,
  // 预览视频地址
  preview: 0,
  // 封面
  cover: '',
  imgcover: '',
  // 内容类型 1：AV 2：三级 3：动漫 4：自拍
  contentType: 1,
  // 时长：秒
  duration: 0,
  // 默认1=免费,2=VIP,3=收费(金币)
  priceType: 1,
  // 免费为0，VIP填用户等级，收费为收费值
  price: 0,
  // VIP等级优惠价
  discountPrice: '',
  // 开启状态 1：开启 0：未开启
  enableStatus: 1,
  // 当前点赞数
  likeNumber: 0,
  // 播放数量
  playNumber: 0,
  // 评论数量
  commentNumber: 0,
  // 所属分类
  categoryPid: '',
  categoryPname: '',
  categoryId: '',
  categoryName: '',
  selectCategoryDatas: '',
  // 关联标签多个标签以空格隔开
  tagList: '',
  // 1：大陆 2：日本 3：韩国 4：欧美 5：台湾 6：港澳
  region: 1,
  // 0：无码 1有码
  mosaicFlag: 0,
  // 字幕 0：无中字幕 1：有中字幕
  subtitleFlag: 0,
  // 作者ID
  actorIds: '',
  // 作者昵称
  actorNames: '',
  // 作者头像
  actorAvatars: '',
  // 视频秘钥
  secretKey: '',
  // 权重
  weight: 1,
  // 番号
  copyRightCode: ''
})
const formRules = reactive({
  title: [
    { required: true, message: '请输入视频的名称', trigger: 'blur' }
  ],
  cover: [
    { required: true, message: '请上传一个封面', trigger: 'blur' }
  ],
  url: [
    { required: true, message: '请上传一个视频', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择一个分类', trigger: 'blur' }
  ]
})
// 表单 Ref
const formRef = ref()

/** 打开弹窗 */
const open = async (type, id) => {
  dialogVisible.value = true
  dialogTitle.value = type
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const response = await getVideoAdmin(id)
      console.log("视频详情",response.data)
      formData.value = response.data
    } finally {
      formLoading.value = false
    }
  }
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })

/** 提交表单 */
// 定义 success 事件，用于操作成功后的回调
const emit = defineEmits(['success'])
const submitForm = async () => {
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
    const data = formData.value
    if (formType.value === 'create') {
      await createNotice(data)
      ElComponent.msgSuccess("创建成功")
    } else {
      await updateNotice(data)
      ElComponent.msgSuccess("更新成功")
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 分类操作弹窗操作 */
const videoCategoryRef = ref()
const handleOpenCategory = () => {
  videoCategoryRef.value.open()
}
/** 标签操作弹窗操作 */
const videoTagRef = ref()
const handleOpenTagDialog = () => {
  videoTagRef.value.open()
}
/** 分类操作的返回 */
const handleVideoSelectCategory = (data) => {
  console.log("啊哈",data)
}
const handleVideoSelectTag = (data) => {
  console.log("啊哈2",data)
}
/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    title: '',
    type: undefined,
    content: '',
    status: CommonStatusEnum.ENABLE,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>

<style scoped>

</style>
