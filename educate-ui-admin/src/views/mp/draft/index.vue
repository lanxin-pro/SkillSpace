<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="68px"
        size="small"
    >
      <el-form-item label="公众号" prop="accountId">
        <WxAccountSelect @change="onAccountChanged" />
      </el-form-item>
      <el-form-item>
        <el-button
            type="primary"
            plain
            @click="handleAdd"
            v-hasPermi="['mp:draft:create']"
            icon="Plus"
            :disabled="accountId === 0"
        >
          新增
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 列表 -->
    <DraftTable
        :loading="loading"
        :list="list"
        @update="onUpdate"
        @delete="onDelete"
        @publish="onPublish"
    />

    <!-- 分页记录 -->
    <Pagination
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 添加或修改草稿对话框 -->
    <el-dialog
        :title="isCreating ? '新建图文' : '修改图文'"
        width="80%"
        v-model="showDialog"
        :before-close="onBeforeDialogClose"
        destroy-on-close
    >
      <NewsForm
          v-model="newsList"
          v-loading="isSubmitting"
          :is-creating="isCreating"
      />
      <template #footer>
        <el-button @click="showDialog = false">取 消</el-button>
        <el-button type="primary" @click="onSubmitNewsItem">提 交</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import Pagination from '@/components/Pagination/index.vue'
import ELComponent from '@/plugins/modal.js'
import { ref, reactive,onMounted,provide } from 'vue'
import WxAccountSelect from '@/views/mp/components/wx-account-select/index.vue'
import { getDraftPage,createDraft,updateDraft,deleteDraft } from '@/api/mp/draft/index.js'
import DraftTable from './components/DraftTable.vue'
import NewsForm from './components/NewsForm.vue'
// TODO j-sentinel 可以用改本地数据模拟，避免API调用超限
import drafts from './mock'

const accountId = ref(-1)
provide('accountId', accountId)

// 列表的加载中
const loading = ref(true)
// 列表的数据
const list = ref([])
// 列表的总页数
const total = ref(0)

const queryParams = reactive({
  pageNo: 1,
  pageSize: 20,
  accountId: accountId
})

// ========== 草稿新建 or 修改 ==========
const showDialog = ref(false)
const newsList = ref([])
const mediaId = ref('')
const isCreating = ref(true)
const isSubmitting = ref(false)

/** 侦听公众号变化 **/
const onAccountChanged = (id) => {
  accountId.value = id
  queryParams.pageNo = 1
  getList()
}

// 关闭弹窗
const onBeforeDialogClose = async (onDone) => {
  try {
    await ELComponent.confirm('修改内容可能还未保存，确定关闭吗?')
    onDone()
  } catch {}
}

// ======================== 列表查询 ========================
/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const enableDrafts = import.meta.env.VITE_APP_MOCK_SERVER
    // 关闭mock才会走后端接口
    if(!enableDrafts){
      const response = await getDraftPage(queryParams)
      drafts = response.data
    }
    drafts.list.forEach((draft) => {
      console.log(draft)
      const newsList = draft.content.newsItem
      console.log("初始化的执行",newsList)
      // 将 thumbUrl 转成 picUrl，保证 wx-news 组件可以预览封面
      newsList.forEach((item) => {
        item.picUrl = item.thumbUrl
      })
    })
    list.value = drafts.list
    total.value = drafts.total
  } finally {
    loading.value = false
  }
}

// ======================== 新增/修改草稿 ========================
/** 新增按钮操作 */
const handleAdd = () => {
  isCreating.value = true
  newsList.value = []
  showDialog.value = true
}

/** 更新按钮操作 */
const onUpdate = (item) => {
  mediaId.value = item.mediaId
  newsList.value = JSON.parse(JSON.stringify(item.content.newsItem))
  isCreating.value = false
  showDialog.value = true
}

/** 提交按钮 */
const onSubmitNewsItem = async () => {
  isSubmitting.value = true
  try {
    if (isCreating.value) {
      await createDraft(accountId.value, newsList.value)
      ELComponent.notifySuccess('新增成功')
    } else {
      await updateDraft(accountId.value, mediaId.value, newsList.value)
      ELComponent.notifySuccess('更新成功')
    }
  } finally {
    showDialog.value = false
    isSubmitting.value = false
    await getList()
  }
}

// ======================== 草稿箱发布 ========================
const onPublish = async (item) => {
  const mediaId = item.mediaId
  const content =
      '你正在通过发布的方式发表内容。 发布不占用群发次数，一天可多次发布。' +
      '已发布内容不会推送给用户，也不会展示在公众号主页中。 ' +
      '发布后，你可以前往发表记录获取链接，也可以将发布内容添加到自定义菜单、自动回复、话题和页面模板中。'
  try {
    await ELComponent.confirm(content)
    await submitFreePublish(accountId.value, mediaId)
    ELComponent.notifySuccess('发布成功')
    await getList()
  } catch {}
}

/** 删除按钮操作 */
const onDelete = async (item) => {
  const mediaId = item.mediaId
  try {
    await ELComponent.confirm('此操作将永久删除该草稿, 是否继续?')
    await deleteDraft(accountId.value, mediaId)
    ELComponent.notifySuccess('删除成功')
    await getList()
  } catch {}
}
</script>

<style lang="scss" scoped>
.pagination {
  float: right;
  margin-right: 25px;
}
</style>
