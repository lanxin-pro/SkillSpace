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
      <VideoSearchCategory
          @select='handleSelectCategory'
      />

      <el-form-item label="内容类型" prop="path">
        <el-select
            v-model='queryParams.contentType'
            size='small'
            @change='handleSearch'
        >
          <el-option
              v-for="item in [{id:'',label:'内容类型'},{id:1,label:'AV'},{id:2,label:'三级'},{id:3,label:'动漫'},{id:4,label:'自拍'}]"
              :key='item.id'
              :label='item.label'
              :value='item.id'>
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="是否收费" prop="type" width="80">
        <el-select
            v-model='queryParams.priceType'
            size='small'
            @change='handleSearch'
        >
          <el-option
              v-for="item in [{id:'',label:'是否收费'},{id:1,label:'免费'},{id:2,label:'VIP'},{id:3,label:'收费(金币)'}]"
              :key='item.id'
              :label='item.label'
              :value='item.id'>
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="发布状态" prop="type" width="80">
        <el-select
            v-model='queryParams.enableStatus'
            size="small"
            @change='handleSearch'
        >
          <el-option
              v-for="item in [{id:'',label:'发布状态'},{id:1,label:'启用'},{id:0,label:'禁用'}]"
              :key='item.id'
              :label='item.label'
              :value='item.id'>
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="选择字幕" prop="type" width="80">
        <el-select
            v-model='queryParams.subtitleFlag'
            @change='handleSearch'
        >
          <el-option
              v-for="item in [{id:'',label:'字幕'},{id:0,label:'无中字幕'},{id:1,label:'有中字幕'}]"
              :key='item.id'
              :label='item.label'
              :value='item.id'>
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="有/无码" prop="type" width="80">
        <el-select
            v-model='queryParams.mosaicFlag'
            @change='handleSearch'
        >
          <el-option
              v-for="item in [{id:'',label:'有/无码'},{id:0,label:'无码'},{id:1,label:'有码'}]"
              :key='item.id'
              :label='item.label'
              :value='item.id'>
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item
          label="地域"
          prop="type"
          width="80"
      >
        <el-select
            v-model='queryParams.region'
            @change='handleSearch'
        >
          <el-option
              v-for="item in [{id:'',label:'地域'},{id:1,label:'大陆'},{id:2,label:'日本'},{id:3,label:'韩国'},{id:4,label:'欧美'},{id:5,label:'台湾'},{id:6,label:'港澳'}]"
              :key='item.id'
              :label='item.label'
              :value='item.id'>
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="其他" prop="createTime">
        <el-input
            placeholder="视频标题、标签、演员"
            v-model='queryParams.keyword'
            maxlength='20'
        />
      </el-form-item>

      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
            v-model="queryParams.createTime"
            value-format="YYYY-MM-DD HH:mm:ss"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8 mt10">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" size="small" @click="openForm(SystemCreateOrUpdate.CREATE)">新建视频</el-button>
      </el-col>
    </el-row>

    <!-- 列表 -->
    <div v-for='(data,index) in list' class="article-card"
         :class="[(checkVideoIds && checkVideoIds.findIndex(v => v === data.videoCode)!=-1) ? 'checked' : '']"
    >
      <a href='javascript:void(0);' @click.prevent="handleOpenPreview(data)"  class='cover-wrp'>
        <el-image
            :src="data.cover"
            fit="contain"
            style="width: 220px;height: 188px;"
            lazy
        />
      </a>
      <div>
        <div class="meta-title">
          <a href='javascript:void(0);' class='name ellipsis fw c333'>{{ data.title }}</a></div>
        <div class="meta-status">
            <el-checkbox-group v-model="checkVideoIds" @change="handleCheckedCitiesChange">
              <el-checkbox :label="data.videoCode"><strong>视频ID：{{data.videoCode}}</strong></el-checkbox>
            </el-checkbox-group>
            <span style="margin-left: 10px;"  v-if='data.tagList'>标签：</span>
            <!--     如果有标签的话，就添加啊     -->
            <span v-if='data.tagList'>
                <el-tag style="margin-left: 5px" v-for="tag in data.tagList.split(',')" size='small' :key='tag'>
                  {{ tag }}
                </el-tag>
            </span>
            <span v-else>
                <el-tag size='small'>
                  暂无
                </el-tag>
            </span>
          <strong style="margin-left:10px;">作者：</strong>
          <span style="color:#666">{{ handleChangeNames(data.actorNames).join('，') }}</span>
        </div>
        <div class='meta-status' v-if='data.categoryId'>
          <strong>视频分类：</strong>
          {{ handleChangeCnameToTags(data.categoryPname, data.categoryName).join('，') }}
        </div>
        <div class='meta-status'>
          <div style="color: #677886;">
            <span>
              <strong>出厂地域：</strong>
              <span v-if='data.region==1'>大陆</span>
              <span v-if='data.region==2'>日本</span>
              <span v-if='data.region==3'>韩国</span>
              <span v-if='data.region==4'>欧美</span>
              <span v-if='data.region==5'>台湾</span>
              <span v-if='data.region==6'>港澳</span>
            </span>
            <span style='padding:0 6px;'>/</span>
            <span style='margin-left: 12px;'><strong>视频类型：</strong>
                                <span>{{filterLabel(data.contentType)}}</span>
                              </span>
            <span style='padding:0 6px;'>/</span>
            <span style='margin-left: 12px;'><strong>有无字幕：</strong>
                                <span v-if='data.subtitleFlag==0'>无字幕</span>
                                <span v-if='data.subtitleFlag==1'>有字幕</span>
                              </span>
            <span style='margin-left: 12px;'>
                                 <span style='padding:0 6px;'>/</span>
                                 <strong>视频有无码：</strong>
                                 <span v-if='data.mosaicFlag==0'>无码</span>
                                 <span v-if='data.mosaicFlag==1'>有码</span>
                              </span>
            <span style='margin-left: 12px;'>
                                 <span style='padding:0 6px;'>/</span>
                                 <strong>收费方式：</strong>
                                 <span v-if='data.priceType==1'>免费</span>
                                 <span v-if='data.priceType==2'>VIP</span>
                                 <span v-if='data.priceType==3'>收费(金币)</span>
                              </span>
            <br>
            <span v-if='data.videoCode'>
                                <strong>视频编号：</strong>
                                <span>{{ data.videoCode }}</span>
                                <span style='padding:0 6px;'>/</span>
                                <strong>权重值：</strong>
                                <span>{{ data.weight }}</span>
                              </span>
          </div>
        </div>
        <div class='meta-footer'>
          <div style="line-height: 1px">
            <div title='播放数量' class='click view-stat'>
              <el-icon style='font-size: 16px; color: rgb(153, 153, 153);'><VideoCamera /></el-icon>
              <span class='icon-text click-text'>{{ data.playNumber }}</span>
            </div>
            <div title='点暂数' class='danmu view-stat'>
              <el-icon style='font-size: 16px; color: rgb(153, 153, 153);'><Star /></el-icon>
              <span class='icon-text'>{{ data.likeNumber }}</span>
            </div>
            <div title='评论数量' class='comment view-stat'>
              <el-icon style='font-size: 16px; color: rgb(153, 153, 153);'><ChatDotRound /></el-icon>
              <span class='icon-text'>{{ data.commentNumber }}</span>
            </div>
          </div>


          <div title='更新时间' class='comment view-stat'>
            <span>最后更新于：{{ formatDate(data.updateTime) }}</span>
            <span style="padding-left:10px;">创建时间：{{ formatDate(data.createTime)}}</span>
          </div>
          <div v-if='data.copyRightCode' title='视频番号' class='comment view-stat'>
            <span>视频番号：{{ data.copyRightCode }}</span>
          </div>
          <div class='view-stat'>
            <strong>来源：</strong>
            <span>
             {{ data.datasource === 1 ? "视频后台" : "陌陌数据"}}
            </span>
          </div>

        </div>
        <div class='meta-status' style="max-width: 800px;overflow: auto">
          <character-more :cdesc='data.intro'></character-more>
        </div>
      </div>
      <div class='meta-view'>
        <el-switch
            style='margin-right: 10px;'
            v-model='data.enableStatus'
            @change="handleChange(data,'enableStatus')"
            :active-value='1'
            :inactive-value='0'>
        </el-switch>
        <div class="between">
          <el-button
              size='small'
              type='danger'
              icon='Delete'
              @click='handleDelete(index, data)'>删除
          </el-button>
        </div>
        <div class="between">
          <el-button
              size='small'
              type='primary'
              icon='Edit'
              @click='handleEdit(SystemCreateOrUpdate.UPDATE, data)'>编辑
          </el-button>
        </div>

      </div>
    </div>

<!--      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
              link
              type="primary"
              size="small"
              icon="View"
              @click="handlePreview(scope.row)"
              v-hasPermi="['infra:config:delete']"
          >
            预览
          </el-button>
          <el-button
              link
              type="success"
              size="small"
              icon="Download"
              @click="handleDownload(scope.row)"
              v-hasPermi="['infra:config:delete']"
          >
            下载
          </el-button>
          <el-button
              link
              type="danger"
              size="small"
              icon="Delete"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['infra:config:delete']"
          >
            删除
          </el-button>
        </template>
      </el-table-column>-->

    <!-- 分页 -->
    <Pagination
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <VideoPlay ref="videoPlayRef"/>

    <!-- 表单弹窗：添加/修改 -->
    <VideoForm ref="formRef" @success="getList" />
  </div>

</template>
<script setup>
import { getVideoPage } from '@/api/video/videoadmin/index.js'
import Pagination from '@/components/Pagination/index.vue'
import { dateFormatter,formatDate } from '@/utils/formatTime'
import { fileSizeFormatter } from '@/utils'
import ELComponent from '@/plugins/modal.js'
import { ref, reactive,onMounted } from 'vue'
import VideoPlay from './VideoPlay.vue'
import VideoForm from './VideoForm.vue'
import CharacterMore from '@/components/CharacterMore/index.vue'
import { SystemCreateOrUpdate } from '@/utils/constants.js'
import VideoSearchCategory from '@/components/VideoSearchCategory/index.vue'

const coverBuff = ref("http://127.0.0.1:9011/server/admin-api/infra/file/4/get/84167ee95454c659d80f330263218f7b47e3eb9f22f22f2bebf686f4a9622580.png")
// 列表的加载中
const loading = ref(true)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])
// 批量操作
const checkVideoIds = ref([])
const queryParams = reactive({
  createTime: '',
  endTime: '',
  priceType: '',
  subtitleFlag: '',
  mosaicFlag: '',
  region: '',
  weight: '',
  enableStatus: '',
  categoryId: '',
  categoryPid: '',
  contentType: '',
  datasource: '',
  keyword: '',
  pageSize: 10,
  pageNum: 1,
  total: 0,
  pages: 0,
  resultList: []
})
// 搜索的表单
const queryFormRef = ref()

/** 初始化 **/
onMounted(() => {
  getList()
})
const videoPlayRef = ref()
/** 视频加载 */
const handleOpenPreview = (data)=>{
  videoPlayRef.value.open(data)
}
/** 批量操作 */
const handleCheckedCitiesChange = (value)=>{
  console.log("批量操作",value)
}
const handleChangeNames = (pnames)=> {
  if (pnames) {
    var ppnames = pnames.split(',')
    var arr = []
    ppnames.forEach((c, index) => {
      arr.push(c)
    })
    return arr
  } else {
    return ['暂无']
  }
}

const handleChangeCnameToTags = (pnames, cnames)=> {
  if (pnames) {
    var ppnames = pnames.split(',')
    var ccnames = cnames ? cnames.split(',') : []
    var arr = []
    ppnames.forEach((c, index) => {
      arr.push(c + ' / ' + ccnames[index])
    })
    return arr
  } else {
    return []
  }
}

const filterLabel = (ctype)=> {
  return  '暂无'
}

/** 预览 **/
const previewRef = ref()
const handlePreview = (row)=>{
  previewRef.value.open(row)
}
/** 查询列表 */
const getList = async ()=>{
  loading.value = true
  try {
    const response = await getVideoPage(queryParams)
    list.value = response.data.list
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}
/** 下载 */
const handleDownload = (row)=>{
  fetch(row.url)
      .then(response => response.blob())
      .then(blob => {
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = row.path
        link.click()
      })
}
/** 删除按钮操作 */
const handleDelete = async (id) => {
  try {
    // 删除的二次确认
    await ELComponent.confirm('您确定要删除吗？')
    // 发起删除
    await deleteFile(id)
    ELComponent.msgSuccess('删除成功')
    // 刷新列表
    await getList()
  } catch {}
}
/** 编辑修改操作 */
const formRef = ref()
const openForm = (index,data) => {
  formRef.value.open(index,data)
}
/** 修改按钮操作 */
const handleEdit = async (index,data) => {
  await openForm(SystemCreateOrUpdate.UPDATE,data.id)
}
/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}


</script>
<style scoped>
.cover-wrp {
  position: relative;
  float: left;

  border-radius: 4px;
  overflow: hidden;
  margin-right: 20px;
  background: #f1f3f7 no-repeat 50%
}

.article-card {
  position: relative;
  min-height: 140px;
  border-radius: 4px;
  background: #fff;
  margin-bottom: 10px;
  padding: 10px;
  display: flex;
  flex-wrap: wrap;
}
.article-card:hover{
  border: 1px dashed #409eff
}

.article-card .meta-title {
  height: 24px;
  line-height: 24px
}

.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 16px;
  line-height: 20px;
  vertical-align: middle;
  color:#333!important;
  font-weight: bold;
}

.meta-status {
  padding: 3px 0;
  font-size: 14px;
  color: #505050;
  display: -webkit-box;
  display: -webkit-flex;
  display: flex;
  -webkit-box-align: center;
  -webkit-align-items: center;
  align-items: center;
}

.article-card.checked{
  border:1px dashed #f56c6c
}

.view-stat {
  display: inline-block;
  float: left;
  margin-right: 20px;
  color: #99a2aa
}


.meta-footer .view-stat {
  display: -webkit-box;
  display: -webkit-flex;
  display: flex;
  -webkit-box-align: center;
  -webkit-align-items: center;
  align-items: center;
  margin-right: 25px
}

.icon-text {
  vertical-align: top;
  margin-left: 5px;
}
.meta-footer {
  padding: 3px 0;
  position: relative;
  font-size: 12px;
  display: flex;
}


.article-card .meta-view {
  position: absolute;
  top: 50%;
  margin-top: -16px;
  right: 10px;
  color: #99a2aa
}
.meta-view {
  display: -webkit-box;
  display: -webkit-flex;
  display: flex;
  right: 0
}
.between {
  display: flex;
  align-items: center;
  margin: 0 6px;
}
</style>
