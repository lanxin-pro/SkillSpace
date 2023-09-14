<template>
  <el-container>
    <el-aside width="40%">
      <div style="width: 100%;height: 100%;margin: 40px auto" class="select-item">

        <div v-for="(news, index) in newsList" :key="index">
          <div
              class="news-main father"
              v-if="index === 0"
              :class="{ activeAddNews: activeNewsIndex === index }"
              @click="activeNewsIndexClick(index)"
          >
            <div class="news-content">
              <img class="material-img" :src="news.thumbUrl" />
              <div class="news-content-title">{{ news.title }}</div>
            </div>
            <div class="child" v-if="newsList.length > 1">

              <el-button type="info" circle size="small" @click="() => moveDownNews(index)">
                <font-awesome-icon icon="fa-solid fa-arrow-down" />
              </el-button>

              <el-button
                  v-if="isCreating"
                  type="danger"
                  circle
                  size="small"
                  icon="delete"
                  @click="() => removeNews(index)"
              >
              </el-button>
            </div>
          </div>
          <div
              class="news-main-item father"
              v-if="index > 0"
              :class="{ activeAddNews: activeNewsIndex === index }"
              @click="activeNewsIndex = index"
          >
            <div class="news-content-item">
              <div class="news-content-item-title">{{ news.title }}</div>
              <div class="news-content-item-img">
                <img class="material-img" :src="news.thumbUrl" width="100%" />
              </div>
            </div>
            <div class="child">
              <el-button
                  v-if="newsList.length > index + 1"
                  circle
                  type="info"
                  size="small"
                  @click="() => moveDownNews(index)"
              >
                <font-awesome-icon icon="fa-solid fa-arrow-down" />
              </el-button>
              <el-button
                  v-if="index > 0"
                  type="info"
                  circle
                  size="small"
                  @click="() => moveUpNews(index)"
              >
                <font-awesome-icon icon="fa-solid fa-arrow-up" />
              </el-button>
              <el-button
                  v-if="isCreating"
                  type="danger"
                  size="small"
                  circle
                  icon="Delete"
                  @click="() => removeNews(index)"
              >
              </el-button>
            </div>
          </div>
        </div>

        <el-row justify="center" class="ope-row">
          <el-button
              type="primary"
              circle
              @click="plusNews"
              icon="Plus"
              v-if="newsList.length < 8 && isCreating"
          >
          </el-button>
        </el-row>
      </div>
    </el-aside>

    <el-main>
      <div v-if="newsList.length > 0">
        <!-- 标题、作者、原文地址 -->
        <el-row :gutter="20">
          <el-input v-model="activeNewsItem.title" placeholder="请输入标题（必填）" />
          <el-input
              v-model="activeNewsItem.author"
              placeholder="请输入作者"
              style="margin-top: 5px"
          />
          <el-input
              v-model="activeNewsItem.contentSourceUrl"
              placeholder="请输入原文地址"
              style="margin-top: 5px"
          />
        </el-row>

        <!-- 封面和摘要 -->
        <el-row :gutter="20">

          <el-col :span="12">
            <CoverSelect
                v-model="activeNewsItem"
                :is-first="activeNewsIndex === 0"
            />
          </el-col>

          <el-col :span="12">
            <p>摘要:</p>
            <el-input
                :rows="8"
                type="textarea"
                v-model="activeNewsItem.digest"
                placeholder="请输入摘要"
                class="digest"
                maxlength="120"
            />
          </el-col>

        </el-row>

        <!-- 富文本编辑器组件 -->
        <el-row>
          <Editor
              v-model="activeNewsItem.content"
              :editor-config="editorConfig"
          />
        </el-row>
      </div>
    </el-main>
  </el-container>
</template>

<script setup>
import { propTypes } from '@/utils/propTypes.js'
import { ref,reactive,inject,computed,watch } from 'vue'
import ELComponent from '@/plugins/modal.js'
import { createEditorConfig } from '../editor-config.js'
import Editor from '@/components/Editor/index.vue'
import CoverSelect from './CoverSelect.vue'

const props = defineProps({
  isCreating: propTypes.bool,
  modelValue: propTypes.any
})
const createEmptyNewsItem = () => {
  return {
    title: '',
    thumbMediaId: '',
    author: '',
    digest: '',
    showCoverPic: '',
    content: '',
    contentSourceUrl: '',
    needOpenComment: '',
    onlyFansCanComment: '',
    thumbUrl: ''
  }
}

const accountId = inject('accountId')

// ========== 文件上传 ==========

// 更改左侧  ！切换 ！
const activeNewsIndexClick = (index)=>{
  console.log("点击触发activeNewsIndexClick",index)
  activeNewsIndex.value = index
}

// 上传永久素材的地址
const UPLOAD_URL = import.meta.env.VITE_BASE_URL + '/admin-api/mp/material/upload-permanent'
const editorConfig = createEditorConfig(UPLOAD_URL, accountId)

// v-model=newsList
const emit = defineEmits(["update:modelValue"])

// 初次会执行监听器
const newsList = computed({
  get() {
    console.log("props.modelValue的值",props.modelValue)
    // 实时监听我的v-model的值，如果model变化就改变（新增 or 修改）
    return props.modelValue.length === 0 ? [createEmptyNewsItem()] : props.modelValue
  },
  set(val) {
    console.log("set",val)
    emit('update:modelValue', val)
  }
})

const activeNewsIndex = ref(0)

/** newsList.value[activeNewsIndex.value] 发生变化，activeNewsItem 将会重新计算并执行其中的逻辑 */
// 修改 CoverSelect 组件的时候 就会触发这个方法，但是不知道为什么在create的时候没有生效
const activeNewsItem = computed(() => {
  console.log("开始监听1当前第几个 -> activeNewsIndex.value",activeNewsIndex.value)
  console.log("开始监听2当前newsList -> newsList.value",newsList.value)
  console.log("开始监听3当前返回的内容 -> newsList.value[activeNewsIndex.value]",newsList.value[activeNewsIndex.value])
  // 第几个图文，从0位开始计算 activeNewsIndex.value 保存的地方？
  return newsList.value[activeNewsIndex.value]
})

// 将图文向下移动
const moveDownNews = (index) => {
  console.log("将图文向下移动")
  const temp = newsList.value[index]
  newsList.value[index] = newsList.value[index + 1]
  newsList.value[index + 1] = temp
  activeNewsIndex.value = index + 1
}

// 将图文向上移动
const moveUpNews = (index) => {
  console.log("将图文向上移动")
  const temp = newsList.value[index]
  newsList.value[index] = newsList.value[index - 1]
  newsList.value[index - 1] = temp
  activeNewsIndex.value = index - 1
}

// 删除指定 index 的图文
const removeNews = async (index) => {
  console.log("删除指定 index 的图文")
  try {
    await ELComponent.confirm('确定删除该图文吗?')
    newsList.value.splice(index, 1)
    if (activeNewsIndex.value === index) {
      activeNewsIndex.value = 0
    }
  } catch {}
}

// 添加一个图文
const plusNews = () => {
  console.log("添加一个图文")
  newsList.value.push(createEmptyNewsItem())
  // activeNewsIndex是从0开始 而 newsList 的length 是从1开始的
  activeNewsIndex.value = newsList.value.length - 1
}
</script>

<style lang="scss" scoped>
.ope-row {
  padding-top: 5px;
  margin-top: 5px;
  text-align: center;
  border-top: 1px solid #eaeaea;
}

.el-row {
  margin-bottom: 20px;
}

.el-row:last-child {
  margin-bottom: 0;
}

.digest {
  display: inline-block;
  width: 100%;
  vertical-align: top;
}

/* 新增图文 */
.news-main {
  width: 100%;
  margin: auto;
  background-color: #fff;
}

.news-content {
  position: relative;
  width: 100%;
  background-color: #acadae;
}

.news-content-title {
  position: absolute;
  bottom: 0;
  left: 0;
  display: inline-block;
  width: 98%;
  height: 25px;
  line-height: 16px;
  padding: 1%;
  overflow: hidden;
  font-size: 14px;
  color: #fff;
  text-overflow: ellipsis;
  white-space: nowrap;
  background-color: black;
  opacity: 0.65;
}

.news-main-item {
  width: 100%;
  padding: 5px 0;
  margin: auto;
  background-color: #fff;
  border-top: 1px solid #eaeaea;
}

.news-content-item {
  position: relative;
  margin-left: -3px;
}

.news-content-item-title {
  display: inline-block;
  width: 70%;
  font-size: 12px;
}

.news-content-item-img {
  display: inline-block;
  width: 25%;
  background-color: #acadae;
}

.select-item {
  width: 60%;
  padding: 10px;
  margin: 0 auto 10px;
  border: 1px solid #eaeaea;

  .activeAddNews {
    border: 5px solid #2bb673;
  }
}

.father .child {
  position: relative;
  bottom: 25px;
  display: none;
  text-align: center;
}

.father:hover .child {
  display: block;
}

.material-img {
  width: 100%;
  height: 100%;
}
</style>
