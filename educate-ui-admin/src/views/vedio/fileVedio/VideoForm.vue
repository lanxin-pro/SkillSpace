<template>
  <Dialog lock-scroll="false" v-model="dialogVisible" :title="dialogTitle" width="1200">
    <el-form
        ref="formRef"
        v-loading="formLoading"
        :model="formData"
        :rules="formRules"
        label-width="80px"
    >
      <div style='margin: 0 0 20px;padding:20px 120px 20px 20px;background: #fafafa;'>
        <div class="h2-title">基础信息</div>
        <div style="display:none">
          <el-input v-model='formData.categoryId' type='hidden'/>
          <el-input v-model='formData.categoryName' type='hidden'/>
          <el-input v-model='formData.categoryPid' type='hidden'/>
          <el-input v-model='formData.categoryPname' type='hidden'/>
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
          <el-input
              v-model='formData.title'
              placeholder='请输入视频名称，<=100字符'
              maxlength='100'
          />
        </el-form-item>
        <el-form-item label='视频介绍：' prop='intro' label-width='200px'>
          <el-input
              type='textarea'
              maxlength='400'
              placeholder='请输入附加参数 少于400字'
              v-model='formData.intro'
          />
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
                  class="ml-tag10"
                  v-for='tag in tagArray'
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
              <el-button
                  v-else
                  class='button-new-tag'
                  size='small'
                  @click='showInput'
              >
                + 新建标签
              </el-button>
            </div>
          </div>
        </el-form-item>
      </div>

      <div style='margin: 0 0 20px;padding:20px 120px 20px 20px;background: #fafafa;'>
        <div class="h2-title">视频&封面</div>
        <el-form-item
            label='视频封面'
            prop='cover'
            required label-width='200px'
        >

          <UploadImg
              :url='formData.cover'
              ref='imguplodRef'
              @success='handleUploadSuccess'
         />
        </el-form-item>

        <el-form-item label='视频信息:' prop='url' required label-width='200px'>
          <VideoUpload
              ref='videoUploadRef'
              @success='handleUploadVideoSuccess'
          />
          <div style='display: flex;justify-content: space-between'>
            <el-input
                v-model='formData.url'
                title="视频原始地址(B)"
                placeholder='视频原始地址'
                style='width: 35%'
            />
            <el-input
                type='number'
                v-model='formData.size'
                title='视频原始大小(B)'
                placeholder='原始大小'
                maxlength='20'
                style='width: 24%;margin: 0 1%;'
            />
            <el-input
                type='number'
                v-model='formData.duration'
                title='视频时长(秒)'
                placeholder='时长：秒'
                maxlength='20'
                style='width: 24%;margin-right: 1%;'
            />
            <el-button
                type='primary'
                icon='Top'
                @click='handleOpenUpload'
                class="mr5"
            >
              上传
            </el-button>
            <el-button
                class="mr5"
                type='danger'
                icon='Delete'
                @click='handleClearVideo'
            >
              清空
            </el-button>
          </div>
        </el-form-item>
        <el-form-item label='视频权重：' label-width='200px'>
          <el-input
              type='number'
              style='width: 49.5%;margin-right: 1%;'
              v-model='formData.weight'
              placeholder='视频权重'
              maxlength='10'
          />
          <el-input
              v-model='formData.secretKey'
              style='width: 49.5%;'
              placeholder='视频秘钥'
              maxlength='100'
          />
        </el-form-item>
        <el-form-item label='标清视频名称：' prop='stanUrl' label-width='200px'>
          <el-input v-model='formData.stanUrl' style='width: 49.5%;margin-right: 1%;' placeholder='标清视频地址' maxlength='200'></el-input>
          <el-input v-model='formData.stanSize' placeholder='标清大小' style='width: 49.5%' maxlength='20'></el-input>
        </el-form-item>
        <el-form-item label='高清视频名称：' prop='highUrl' label-width='200px'>
          <el-input
              v-model='formData.highUrl'
              placeholder='高清视频地址'
              style='width: 49.5%;margin-right: 1%;'
              maxlength='200'
          />
          <el-input
              type='number'
              v-model='formData.highSize'
              placeholder='高清大小'
              style='width: 49.5%'
              maxlength='20'
          />
        </el-form-item>
        <el-form-item label='超清视频名称：' prop='superUrl' label-width='200px'>
          <el-input v-model='formData.superUrl' placeholder='超清视频地址' style='width: 49.5%;margin-right: 1%;'
                    maxlength='200'></el-input>
          <el-input v-model='formData.superSize' placeholder='超清大小' style='width: 49.5%' maxlength='20'></el-input>
        </el-form-item>
      </div>


      <div style='margin: 0 0 20px;padding:20px 120px 20px 20px;background: #fafafa;'>
        <div class="h2-title">视频相关</div>
        <el-form-item label='视频番号：' label-width='200px'>
          <el-input v-model='formData.copyRightCode' placeholder='视频番号' maxlength='60'></el-input>
        </el-form-item>
        <el-form-item label='请选择内容类型：' label-width='200px'>
          <el-select style='margin:  0 5px;width: 100%' v-model='formData.contentType' placeholder='请选择内容类型'>
            <el-option
                v-for="item in dynamicsData"
                :key='item.id'
                :label='item.label'
                :value='item.id'>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label='请选择是否收费：' label-width='200px'>
          <el-select style='margin:  0 5px;width: 100%' v-model='formData.priceType' placeholder='请选择是否收费'>
            <el-option
                v-for="item in [{id:'',label:'请选择是否收费'},{id:1,label:'免费'},{id:2,label:'VIP'},{id:3,label:'收费(金币)'}]"
                :key='item.id'
                :label='item.label'
                :value='item.id'>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label='收费价格：' label-width='200px' v-if='formData.priceType == 3'>
          <el-input type='text' v-model='formData.price' placeholder='免费为0，VIP填用户等级，收费为收费值' maxlength='20'
                    style='width: 20%;margin-left: 10px;'></el-input>
        </el-form-item>
        <el-form-item label='优惠价格：' label-width='200px' v-if='formData.priceType == 3'>
          <el-input type='number' v-model='formData.discountPrice' placeholder='VIP等级优惠价' maxlength='20'
                    style='width: 20%;margin-left: 10px;'></el-input>
        </el-form-item>
        <el-form-item label='内容类型：' label-width='200px'>
          <el-select style='margin:  0 5px;width: 100%' v-model='formData.enableStatus' placeholder='请选择发布状态'>
            <el-option
                v-for="item in [{id:1,label:'启用'},{id:0,label:'禁用'}]"
                :key='item.id'
                :label='item.label'
                :value='item.id'>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label='请选择字幕：' label-width='200px'>
          <el-select style='margin:  0 5px;width: 100%' v-model='formData.subtitleFlag' placeholder='请选择字幕'>
            <el-option
                v-for="item in [{id:0,label:'无中字幕'},{id:1,label:'有中字幕'}]"
                :key='item.id'
                :label='item.label'
                :value='item.id'>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label='请选择有/无码：' label-width='200px'>
          <el-select style='margin:  0 5px;width: 100%' v-model='formData.mosaicFlag' placeholder='请选择有/无码'>
            <el-option
                v-for="item in [{id:0,label:'无码'},{id:1,label:'有码'}]"
                :key='item.id'
                :label='item.label'
                :value='item.id'>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label='请选择地域：' label-width='200px'>
          <el-select style='margin:  0 5px;width: 100%' v-model='formData.region' placeholder='请选择地域'>
            <el-option
                v-for="item in [{id:1,label:'大陆'},{id:2,label:'日本'},{id:3,label:'韩国'},{id:4,label:'欧美'},{id:5,label:'台湾'},{id:6,label:'港澳'}]"
                :key='item.id'
                :label='item.label'
                :value='item.id'>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label='播放数量：' label-width='200px'>
          <el-input type='number' v-model='formData.playNumber' placeholder='播放数量' maxlength='20'></el-input>
        </el-form-item>
        <el-form-item label='当前点赞数：' label-width='200px'>
          <el-input type='number' v-model='formData.likeNumber' placeholder='当前点赞数' maxlength='20'></el-input>
        </el-form-item>
        <el-form-item label='演员明星：' label-width='200px'>
          <el-tag
              v-for='(i,index) in actorIdssArr'
              :key='actorIdssArr[index]'
              closable
              @close='handleCloseAuthor(actorIdssArr[index],index)'>
              <span>
              <img style='width: 20px;height: 20px'   v-image-decrypt='actorAvatarsArr[index]' />
              {{ actorIdssArr[index] }} /{{ actorNamessArr[index] }}
              </span>
          </el-tag>
          <el-button  size="mini" icon="el-icon-plus" type="primary" @click='handleOpenedActor' v-re-click >选择作者</el-button>
        </el-form-item>

      </div>







    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>

    <VideoCategory ref="videoCategoryRef" @select='handleVideoSelectCategory'></VideoCategory>
    <VideoTag ref="videoTagRef" @select='handleVideoSelectTag'></VideoTag>

  </Dialog>
</template>

<script setup>
import { ref,reactive,nextTick } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict.js'
import { getSimpleMenusList,getMenu,createMenu,updateMenu } from '@/api/system/menu/index.js'
import { getVideoAdmin } from '@/api/video/videoadmin/index.js'
import { CommonStatusEnum, SystemMenuTypeEnum } from '@/utils/constants.js'
import ElComponent from '@/plugins/modal.js'
import Editor from '@/components/Editor/index.vue'
import VideoCategory from './components/VideoCategory.vue'
import VideoTag from './components/VideoTag.vue'
import UploadImg from '@/components/UploadImg/index.vue'
import VideoUpload from '@/components/VideoUpload/index.vue'
import { SystemCreateOrUpdate } from '@/utils/constants.js'

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
// 标签输入的内容
const inputValue = ref()
// 标签名
const tagArray = ref([])
const formData = reactive({
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
  videoCategoryRef.value.open("添加分类")
}
/** 标签操作弹窗操作 */
const videoTagRef = ref()
const handleOpenTagDialog = () => {
  videoTagRef.value.open("选择标签")
}
/** 分类操作的返回 */
const handleVideoSelectCategory = (data) => {
  console.log("啊哈",data)
}
/** 标签操作的返回 */
const handleVideoSelectTag = (data) => {
  console.log("啊哈2",data)
}
// 是否打开新建标签的输入栏
const inputVisible = ref(false)
// 标签输入栏的ref
const saveTagInput = ref()
/** 新建标签 */
const showInput = () => {
  inputVisible.value = true
  nextTick(_ => {
    saveTagInput.value.focus()
    console.log(saveTagInput.value.focus())
  })
}
/** 标签的blur和@keyup.enter.native事件*/
const  handleInputConfirm = () => {
  if (!inputValue.value) {
    ElComponent.msgError('请输入标签！！！')
    return
  }
  // 不重复就添加
  if (inputValue.value && tagArray.value.indexOf(inputValue.value) === -1) {
    tagArray.value.push(inputValue.value)
    formData.tagList = uniqueArray(tagArray.value).join(',')
    inputVisible.value = false
    inputValue.value = ''
  } else {
    ElComponent.msgError('标签已经存在!!')
  }
}
/** 处理string数组，以，分割 */
const uniqueArray = (arr) => {
  const newArr = []
  for (let i = 0; i < arr.length; i++) {
    if (newArr.indexOf(arr[i]) === -1) {
      newArr.push(arr[i])
    }
  }
  return newArr
}
const videoUploadRef = ref()
/** 视频上传 */
const handleOpenUpload = () => {
  videoUploadRef.value.expandUpload()
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
.button-new-tag:not(:first-child) {
  margin-left: 10px;
  padding-top: 0;
  padding-bottom: 0;
}
.ml-tag10:not(:first-child) {
  margin-left: 10px;
}
.h2-title {
  padding: 20px 0 20px 120px;
  font-size: 22px;
  font-weight: bold;
}
</style>
