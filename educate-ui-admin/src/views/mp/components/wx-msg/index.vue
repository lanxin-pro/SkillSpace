<template>
  <div class="msg-div" ref="msgDivRef">
    <!-- 加载更多 -->

    <!--  加载动画  -->
    <div v-loading="loading"></div>
    <div v-if="!loading">
      <div class="el-table__empty-block" v-if="hasMore" @click="loadMore">
        <span class="el-table__empty-text">点击加载更多</span>
      </div>
      <div class="el-table__empty-block" v-if="!hasMore">
        <span class="el-table__empty-text">没有更多了</span>
      </div>
    </div>

    <!-- 消息列表 -->
    <MsgList :list="list" :account-id="accountId" :user="user" />
  </div>

  <!-- 消息发送栏 -->
  <div class="msg-send" v-loading="sendLoading">
    <WxReplySelect ref="replySelectRef" v-model="reply" />
    <el-button type="success" class="send-but" @click="sendMsg">发送(S)</el-button>
  </div>
</template>

<script setup>
import { ref,reactive,onMounted,unref,nextTick } from 'vue'
import { getMessagePage,sendMessage } from '@/api/mp/message/index.js'
import { getUser } from '@/api/mp/mpuser/index.js'
import WxReplySelect from '@/views/mp/components/wx-reply/index.vue'
import MsgList from './components/MsgList.vue'
import profile from '@/assets/imgs/profile.jpg'
import { ReplyType } from '@/utils/constants.js'
import ELComponent from '@/plugins/modal.js'

const props = defineProps({
  userId: {
    type: Number,
    required: true
  }
})

// 公众号ID，需要通过userId初始化
const accountId = ref(-1)
// 消息列表是否正在加载中
const loading = ref(false)
// 是否可以加载更多
const hasMore = ref(true)
// 消息列表
const list = ref([])
const queryParams = reactive({
  // 当前页数
  pageNo: 1,
  // 每页显示多少条
  pageSize: 15,
  accountId: accountId
})

// 由于微信不再提供昵称，直接使用“用户”展示
const user = reactive({
  nickname: '用户',
  avatar: 'http://127.0.0.1:9011/server/admin-api/infra/file/4/get/b082ad90dc7724530632d2544277d0ddbac4e9d754d12fa36d2fca1518350096.png',
  // 公众号账号编号
  accountId: accountId
})

// ========= 消息发送 =========

// 发送消息是否加载中
const sendLoading = ref(false)
// 微信发送消息
const reply = ref({
  type: ReplyType.Text,
  accountId: -1,
  articles: []
})

// WxReplySelect组件ref，用于消息发送成功后清除内容
const replySelectRef = ref()
// 消息显示窗口ref，用于滚动到底部
const msgDivRef = ref()

/** 完成加载 */
onMounted(async () => {
  const response = await getUser(props.userId)
  console.log("用户信息",response.data)
  const data = response.data
  user.nickname = data.nickname?.length > 0 ? data.nickname : user.nickname
  user.avatar = user.avatar === undefined ? data.avatar : user.avatar
  console.log("结果", user.avatar?.length > 0 ? data.avatar : user.avatar)
  accountId.value = data.accountId
  reply.value.accountId = data.accountId

  refreshChange()
})

// 执行发送
const sendMsg = async () => {
  if (!unref(reply)) {
    return
  }
  // 公众号限制：客服消息，公众号只允许发送一条
  if (
      reply.value.type === ReplyType.News &&
      reply.value.articles &&
      reply.value.articles.length > 1
  ) {
    reply.value.articles = [reply.value.articles[0]]
    ELComponent.msgSuccess('图文消息条数限制在 1 条以内，已默认发送第一条')
  }

  const data = await sendMessage({ userId: props.userId, ...reply.value })
  sendLoading.value = false

  list.value = [...list.value, ...[data]]
  await scrollToBottom()

  // 发送后清空数据
  replySelectRef.value?.clear()
}

const loadMore = () => {
  queryParams.pageNo++
  getPage(queryParams, null)
}

const getPage = async (page, params = null) => {
  loading.value = true
  let dataTemp = await getMessagePage(
      Object.assign(
          {
            pageNo: page.pageNo,
            pageSize: page.pageSize,
            userId: props.userId,
            accountId: page.accountId
          },
          params
      )
  )

  const scrollHeight = msgDivRef.value?.scrollHeight ?? 0
  // 处理数据
  const data = dataTemp.data.list.reverse()
  list.value = [...data, ...list.value]
  loading.value = false
  if (data.length < queryParams.pageSize || data.length === 0) {
    hasMore.value = false
  }
  queryParams.pageNo = page.pageNo
  queryParams.pageSize = page.pageSize
  // 滚动到原来的位置
  if (queryParams.pageNo === 1) {
    // 定位到消息底部
    await scrollToBottom()
  } else if (data.length !== 0) {
    // 定位滚动条
    await nextTick()
    if (scrollHeight !== 0) {
      if (msgDivRef.value) {
        msgDivRef.value.scrollTop = msgDivRef.value.scrollHeight - scrollHeight - 100
      }
    }
  }
}

const refreshChange = () => {
  getPage(queryParams)
}

/** 定位到消息底部 */
const scrollToBottom = async () => {
  await nextTick()
  if (msgDivRef.value) {
    msgDivRef.value.scrollTop = msgDivRef.value.scrollHeight
  }
}
</script>

<style lang="scss" scoped>
.msg-div {
  height: 50vh;
  margin-right: 10px;
  margin-left: 10px;
  overflow: auto;
  background-color: #eaeaea;
}

.msg-send {
  padding: 10px;
}

.send-but {
  float: right;
  margin-top: 8px;
  margin-bottom: 8px;
}
</style>
