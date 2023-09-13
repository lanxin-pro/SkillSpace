<template>
  <div class="pb-30px">
    <!-- 类型：image -->
    <div v-if="props.type === 'image'">
      <div class="waterfall" v-loading="loading">
        <div class="waterfall-item" v-for="item in list" :key="item.mediaId">
          <img class="material-img" :src="item.url" />
          <p class="item-name">{{ item.name }}</p>
          <el-row class="ope-row">
            <el-button type="success" @click="selectMaterialFun(item)">
              选择
              <Icon icon="ep:circle-check" />
            </el-button>
          </el-row>
        </div>
      </div>
      <!-- 分页组件 -->
      <Pagination
          :total="total"
          v-model:page="queryParams.pageNo"
          v-model:limit="queryParams.pageSize"
          @pagination="getMaterialPageFun"
      />
    </div>
    <!-- 类型：voice -->
    <div v-else-if="props.type === 'voice'">
      <!-- 列表 -->
      <el-table v-loading="loading" :data="list">
        <el-table-column label="编号" align="center" prop="mediaId" />
        <el-table-column label="文件名" align="center" prop="name" />
        <el-table-column label="语音" align="center">
          <template #default="scope">
            <WxVoicePlayer :url="scope.row.url" />
          </template>
        </el-table-column>
        <el-table-column
            label="上传时间"
            align="center"
            prop="createTime"
            width="180"
            :formatter="dateFormatter"
        />
        <el-table-column label="操作" align="center" fixed="right">
          <template #default="scope">
            <el-button type="primary" link @click="selectMaterialFun(scope.row)"
            >选择
              <Icon icon="ep:plus" />
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页组件 -->
      <Pagination
          :total="total"
          v-model:page="queryParams.pageNo"
          v-model:limit="queryParams.pageSize"
          @pagination="getPage"
      />
    </div>
    <!-- 类型：video -->
    <div v-else-if="props.type === 'video'">
      <!-- 列表 -->
      <el-table v-loading="loading" :data="list">
        <el-table-column label="编号" align="center" prop="mediaId" />
        <el-table-column label="文件名" align="center" prop="name" />
        <el-table-column label="标题" align="center" prop="title" />
        <el-table-column label="介绍" align="center" prop="introduction" />
        <el-table-column label="视频" align="center">
          <template #default="scope">
            <WxVideoPlayer :url="scope.row.url" />
          </template>
        </el-table-column>
        <el-table-column
            label="上传时间"
            align="center"
            prop="createTime"
            width="180"
            :formatter="dateFormatter"
        />
        <el-table-column
            label="操作"
            align="center"
            fixed="right"
            class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button type="primary" link @click="selectMaterialFun(scope.row)"
            >选择
              <Icon icon="akar-icons:circle-plus" />
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页组件 -->
      <Pagination
          :total="total"
          v-model:page="queryParams.pageNo"
          v-model:limit="queryParams.pageSize"
          @pagination="getMaterialPageFun"
      />
    </div>
    <!-- 类型：news -->
    <div v-else-if="props.type === 'news'">
      <div class="waterfall" v-loading="loading">
        <div class="waterfall-item" v-for="item in list" :key="item.mediaId">
          <div v-if="item.content && item.content.newsItem">
            <WxNews :articles="item.content.newsItem" />
            <el-row class="ope-row">
              <el-button type="success" @click="selectMaterialFun(item)">
                选择
                <Icon icon="ep:circle-check" />
              </el-button>
            </el-row>
          </div>
        </div>
      </div>
      <!-- 分页组件 -->
      <Pagination
          :total="total"
          v-model:page="queryParams.pageNo"
          v-model:limit="queryParams.pageSize"
          @pagination="getMaterialPageFun"
      />
    </div>
  </div>
</template>

<script setup>
import { ref,reactive,onMounted } from 'vue'
import { propTypes } from '@/utils/propTypes.js'
import { NewsType } from '@/utils/constants.js'
import { getMaterialPage } from '@/api/mp/material/index.js'
import { getDraftPage } from '@/api/mp/draft/index.js'
import Pagination from '@/components/Pagination/index.vue'

const props = defineProps(
    ({
      type: propTypes.string,
      accountId: propTypes.number,
      newsType: NewsType,
    })
)

const emit = defineEmits(['select-material'])

// 遮罩层
const loading = ref(false)
// 总条数
const total = ref(0)
// 数据列表
const list = ref([])
// 查询参数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  accountId: props.accountId
})

const selectMaterialFun = (item) => {
  emit('select-material', item)
}

onMounted(async () => {
  await getPage()
})

const getPage = async () => {
  loading.value = true
  try {
    if (props.type === 'news' && props.newsType === NewsType.Published) {
      // 【图文】+ 【已发布】
      await getFreePublishPageFun()
    } else if (props.type === 'news' && props.newsType === NewsType.Draft) {
      // 【图文】+ 【草稿】
      await getDraftPageFun()
    } else {
      // 【素材】
      await getMaterialPageFun()
    }
  } finally {
    loading.value = false
  }
}

const getMaterialPageFun = async () => {
  const response = await getMaterialPage({
    ...queryParams,
    type: props.type
  })
  list.value = response.data.list
  total.value = response.data.total
}

const getFreePublishPageFun = async () => {
  const data = await getFreePublishPage(queryParams)
  data.list.forEach((item) => {
    const articles = item.content.newsItem
    articles.forEach((article) => {
      article.picUrl = article.thumbUrl
    })
  })
  list.value = data.list
  total.value = data.total
}

const getDraftPageFun = async () => {
  const response = await getDraftPage(queryParams)
  response.data.list.forEach((draft) => {
    const articles = draft.content.newsItem
    articles.forEach((article) => {
      article.picUrl = article.thumbUrl
    })
  })
  list.value = response.data.list
  total.value = response.data.total
}

</script>
<style lang="scss" scoped>
@media (width >= 992px) and (width <= 1300px) {
  .waterfall {
    column-count: 3;
  }

  p {
    color: red;
  }
}

@media (width >= 768px) and (width <= 991px) {
  .waterfall {
    column-count: 2;
  }

  p {
    color: orange;
  }
}

@media (width <= 767px) {
  .waterfall {
    column-count: 1;
  }
}

.waterfall {
  width: 100%;
  column-gap: 10px;
  column-count: 5;
  margin: 0 auto;
}

.waterfall-item {
  padding: 10px;
  margin-bottom: 10px;
  break-inside: avoid;
  border: 1px solid #eaeaea;
}

.material-img {
  width: 100%;
}

p {
  line-height: 30px;
}
</style>
