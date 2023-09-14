<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        class="-mb-15px"
        :inline="true"
        label-width="68px"
        size="small"
    >
      <el-form-item label="公众号" prop="accountId">
        <WxAccountSelect @change="onAccountChanged" />
      </el-form-item>
    </el-form>


    <el-tabs
        style="margin-top: 10px"
        v-model="type"
        @tab-change="onTabChange"
    >
      <!-- tab 1：图片  -->
      <el-tab-pane :name="UploadType.Image">
        <template #label>
          <el-row align="middle">
            <font-awesome-icon
                class="mr5"
                icon="fa-regular fa-image"
            />
            图片
          </el-row>
        </template>

        <!--    上传组件    -->
        <UploadFile
            v-hasPermi="['mp:material:upload-permanent']"
            :type="UploadType.Image"
            @uploaded="getList"
            :account-id="queryParams.accountId"
        >
          支持 bmp/png/jpeg/jpg/gif 格式，大小不超过 2M
        </UploadFile>

        <!-- 列表 -->
        <ImageTable
            :loading="loading"
            :list="list"
            @delete="handleDelete"
        />

        <!-- 分页组件 -->
        <Pagination
            :total="total"
            v-model:page="queryParams.pageNo"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
        />
      </el-tab-pane>

      <!-- tab 2：语音  -->
      <el-tab-pane :name="UploadType.Voice">
        <template #label>
          <el-row align="middle">
            <font-awesome-icon
                class="mr5"
                icon="fa-solid fa-microphone"
            />
            语音
          </el-row>
        </template>
        <UploadFile
            v-hasPermi="['mp:material:upload-permanent']"
            :type="UploadType.Voice"
            @uploaded="getList"
        >
          格式支持 mp3/wma/wav/amr，文件大小不超过 2M，播放长度不超过 60s
        </UploadFile>
        <!-- 列表 -->
        <VoiceTable :list="list" :loading="loading" @delete="handleDelete" />
        <!-- 分页组件 -->
        <Pagination
            :total="total"
            v-model:page="queryParams.pageNo"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
        />
      </el-tab-pane>

      <!-- tab 3：视频 -->
      <el-tab-pane :name="UploadType.Video">
        <template #label>
          <el-row align="middle">
            <font-awesome-icon
                class="mr5"
                icon="fa-solid fa-file-video" />
            视频
          </el-row>
        </template>
        <el-button
            v-hasPermi="['mp:material:upload-permanent']"
            type="primary"
            plain
            @click="showCreateVideo = true"
            icon="Plus"
            size="small">
          新建视频
        </el-button>
        <!-- 新建视频的弹窗 -->
        <UploadVideo v-model="showCreateVideo" />
        <!-- 列表 -->
        <VideoTable :list="list" :loading="loading" @delete="handleDelete" />
        <!-- 分页组件 -->
        <Pagination
            :total="total"
            v-model:page="queryParams.pageNo"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
        />
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script setup>
import Pagination from '@/components/Pagination/index.vue'
import ELComponent from '@/plugins/modal.js'
import { ref, reactive, onMounted, provide } from 'vue'
import WxAccountSelect from '@/views/mp/components/wx-account-select/index.vue'
import { getMaterialPage,deletePermanentMaterial } from '@/api/mp/material/index.js'
import ImageTable from './components/ImageTable.vue'
import VoiceTable from './components/VoiceTable.vue'
import VideoTable from './components/VideoTable.vue'
import UploadFile from './components/UploadFile.vue'
import UploadVideo from './components/UploadVideo.vue'
import { UploadType } from '@/utils/constants.js'

// 素材类型
const type = ref(UploadType.Image)
// 遮罩层
const loading = ref(false)
// 总条数
const list = ref([])
// 数据列表
const total = ref(0)
// 查询参数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 20,
  accountId: -1,
  permanent: true
})

// 是否新建视频的弹窗
const showCreateVideo = ref(false)

/** 侦听公众号变化 **/
const onAccountChanged = (id) => {
  queryParams.accountId = id
  queryParams.pageNo = 1
  getList()
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    // 解构传递参数
    const response = await getMaterialPage({
      ...queryParams,
      type: type.value
    })
    list.value = response.data.list
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 处理 table 切换 */
const onTabChange = () => {
  // 提前情况数据，避免 tab 切换后显示其他tab节点的数据
  list.value = []
  total.value = 0
  // 从第一页开始查询
  handleQuery()
}

/** 处理删除操作 */
const handleDelete = async (id) => {
  await ELComponent.confirm('此操作将永久删除该文件, 是否继续?')
  await deletePermanentMaterial(id)
  ELComponent.alertSuccess('删除成功')
}
</script>

<style scoped>

</style>
