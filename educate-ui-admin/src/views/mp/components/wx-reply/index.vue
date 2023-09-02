<template>
  <el-tabs type="border-card" v-model="currentTab">
    <!-- 类型 1：文本 -->
    <el-tab-pane :name="ReplyType.Text">
      <template #label>
        <el-row align="middle"><Icon icon="ep:document" /> 文本</el-row>
      </template>
      <TabText v-model="reply" />
    </el-tab-pane>

    <!-- 类型 2：图片 -->
    <el-tab-pane :name="ReplyType.Image">
      <template #label>
        <el-row align="middle"><Icon icon="ep:picture" class="mr-5px" /> 图片</el-row>
      </template>
      <TabImage v-model="reply" />
    </el-tab-pane>

    <!-- 类型 3：语音 -->
    <el-tab-pane :name="ReplyType.Voice">
      <template #label>
        <el-row align="middle"><Icon icon="ep:phone" /> 语音</el-row>
      </template>
      <TabVoice v-model="reply" />
    </el-tab-pane>

    <!-- 类型 4：视频 -->
    <el-tab-pane :name="ReplyType.Video">
      <template #label>
        <el-row align="middle"><Icon icon="ep:share" /> 视频</el-row>
      </template>
      <TabVideo v-model="reply" />
    </el-tab-pane>

    <!-- 类型 5：图文 -->
    <el-tab-pane :name="ReplyType.News">
      <template #label>
        <el-row align="middle"><Icon icon="ep:reading" /> 图文</el-row>
      </template>
      <TabNews v-model="reply" :news-type="newsType" />
    </el-tab-pane>

    <!-- 类型 6：音乐 -->
    <el-tab-pane :name="ReplyType.Music">
      <template #label>
        <el-row align="middle"><Icon icon="ep:service" />音乐</el-row>
      </template>
      <TabMusic v-model="reply" />
    </el-tab-pane>
  </el-tabs>
</template>

<script setup>
import { withDefaults,computed,watch,unref,ref } from 'vue'
import TabText from './components/TabText.vue'
import TabImage from './components/TabImage.vue'
import TabVoice from './components/TabVoice.vue'
import TabVideo from './components/TabVideo.vue'
import TabNews from './components/TabNews.vue'
import TabMusic from './components/TabMusic.vue'
import { ReplyType,NewsType } from '@/utils/constants.js'
import { propTypes } from '@/utils/propTypes.js'

// withDefaults 是一个用于设置属性默认值的函数。它接受两个参数：第一个参数是定义的属性对象，第二个参数是一个包含默认值的对象。
/*const props = withDefaults(defineProps({
  modelValue: propTypes.any,
  newsType: NewsType
}), {
  newsType: () => NewsType.Published
})*/

const props = defineProps({
  modelValue: propTypes.any,
  newsType: NewsType
})
const emit = defineEmits({})

const reply = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})
// 作为多个标签保存各自Reply的缓存
const tabCache = new Map()
// 采用独立的ref来保存当前tab，避免在watch标签变化，对reply进行赋值会产生了循环调用
const currentTab = ref(props.modelValue?.type || ReplyType.Text)

watch(
    currentTab,
    (newTab, oldTab) => {
      // 第一次进入：oldTab 为 undefined
      // 判断 newTab 是因为 Reply 为 Partial
      if (oldTab === undefined || newTab === undefined) {
        return
      }

      tabCache.set(oldTab, unref(reply))

      // 从缓存里面取出新tab内容，有则覆盖Reply，没有则创建空Reply
      const temp = tabCache.get(newTab)
      if (temp) {
        reply.value = temp
      } else {
        let newData = createEmptyReply(reply)
        newData.type = newTab
        reply.value = newData
      }
    },
    {
      immediate: true
    }
)

/** 清除除了`type`, `accountId`的字段 */
const clear = () => {
  reply.value = createEmptyReply(reply)
}

/** 利用旧的reply[accountId, type]初始化新的Reply */
const createEmptyReply = (old) => {
  return {
    accountId: unref(old).accountId,
    type: unref(old).type,
    name: null,
    content: null,
    mediaId: null,
    url: null,
    title: null,
    description: null,
    thumbMediaId: null,
    thumbMediaUrl: null,
    musicUrl: null,
    hqMusicUrl: null,
    introduction: null,
    articles: []
  }
}

defineExpose({
  clear
})
</script>

<style scoped>
.select-item {
  width: 280px;
  padding: 10px;
  margin: 0 auto 10px;
  border: 1px solid #eaeaea;
}

.select-item2 {
  padding: 10px;
  margin: 0 auto 10px;
  border: 1px solid #eaeaea;
}

.ope-row {
  padding-top: 10px;
  text-align: center;
}

.input-margin-bottom {
  margin-bottom: 2%;
}

.item-name {
  overflow: hidden;
  font-size: 12px;
  text-align: center;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.el-form-item__content {
  line-height: unset !important;
}

.col-select {
  width: 49.5%;
  height: 160px;
  padding: 50px 0;
  border: 1px solid rgb(234 234 234);
}

.col-select2 {
  height: 160px;
  padding: 50px 0;
  border: 1px solid rgb(234 234 234);
}

.col-add {
  float: right;
  width: 49.5%;
  height: 160px;
  padding: 50px 0;
  border: 1px solid rgb(234 234 234);
}

.avatar-uploader-icon {
  width: 100px !important;
  height: 100px !important;
  font-size: 28px;
  line-height: 100px !important;
  color: #8c939d;
  text-align: center;
  border: 1px solid #d9d9d9;
}

.material-img {
  width: 100%;
}

.thumb-div {
  display: inline-block;
  text-align: center;
}

.item-infos {
  width: 30%;
  margin: auto;
}
</style>
