<template>
  <!--  页面分为四个区域，分别是Header、Aside、Main、Footer  -->
  <el-aside class="conversation-container" style="height: 100%">
    <!-- 左顶部：对话 -->
    <div style="height: 100%">
      <el-button class="w-1/1 btn-new-conversation" icon="plus" type="primary" @click="craete">新建对话</el-button>
      <el-input placeholder="搜索历史记录" size="large" @keyup="searchConversation">
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>

      <div class="conversation-list">
        <el-empty v-if="loading" v-loading="loading" description=""/>
        <div v-for="conversationKey in Object.keys(conversationMap)" :key="conversationKey">
          <!--     时间的展示     -->
          <div
              v-if="conversationMap[conversationKey].length"
              class="conversation-item classify-title"
          >
            <el-text class="mx-1" size="small" tag="b">{{ conversationKey }}</el-text>
          </div>
        <!--     每个时间段的数据     -->
          <div v-for="conversation in conversationMap[conversationKey]"  :key="conversation.id"
               class="conversation-item">
            <div :class="
                conversation.id === activeConversationId ? 'conversation active' : 'conversation'
              ">
              <div class="title-wrapper">
                <img :src="conversation.roleAvatar || roleAvatarDefaultImg" class="avatar" />
                <span class="title">{{ conversation.title }}</span>
              </div>
            </div>
          </div>


        </div>
        <!--  底部站位  -->
        <div style="height: 160px; width: 100%"></div>
        <div>
          <div class="conversation-item">
            内容
          </div>
        </div>
      </div>
    </div>

    <div>
      对话
    </div>
    <div>
      底部
    </div>
  </el-aside>
</template>

<script setup>
import { ref, reactive, onMounted, toRefs } from 'vue'
import { getChatConversationMyList } from '@/api/ai/chat/conversation'
import { propTypes } from '@/utils/propTypes.js'

// 对话分组 (置顶、今天、三天前、一星期前、一个月前)
const conversationMap = ref([])
// 对话列表
const conversationList = ref([])
// 选中的对话，默认为 null
const activeConversationId = ref(null)
// 加载中
const loading = ref(false)
const props = defineProps({
  activeId: propTypes.string.required
})

/**
 * 对话 - 获取列表
 * @returns {Promise<void>}
 */
const getChatConversationList = async () => {
  // 1. 获取 对话数据
  const response = await getChatConversationMyList()
  // 2. 排序
  response.data.sort((a, b) => {
    return b.createTime - a.createTime
  })
  conversationList.value = response.data
  // 3. 默认选中
  if(!activeId?.value) {

  }
  // 4. 没有任何对话情况
  if (conversationList.value.length === 0) {
    activeConversationId.value = null
    conversationMap.value = {}
    return
  }
  // 5. 对话根据时间分组(置顶、今天、一天前、三天前、七天前、30天前)
  conversationMap.value = await conversationTimeGroup(conversationList.value)
}

onMounted(async () => {
  // 获取对话 列表
  await getChatConversationList()
})

const conversationTimeGroup = async (list) => {
    // 排序、指定、时间分组(今天、一天前、三天前、七天前、30天前)
  const groupMap = {
    '置顶': [],
    '今天': [],
    '一天前': [],
    '三天前': [],
    '七天前': [],
    '三十天前': []
  }
  // 当前时间的时间戳
  const now = Date.now()
  // 定义时间间隔常量（单位：毫秒）
  const oneDay = 24 * 60 * 60 * 1000
  const threeDays = 3 * oneDay
  const sevenDays = 7 * oneDay
  const thirtyDays = 30 * oneDay

  for (const conversation of list) {
    console.log(
        conversation.updateTime
    )
    // 置顶
    if (conversation.pinned) {
      groupMap['置顶'].push(conversation)
      continue
    }
    // 计算时间差（单位：毫秒）
    const diff = now - conversation.updateTime
    console.log('不同', diff)
    // 根据时间间隔判断
    if (diff < oneDay) {
      groupMap['今天'].push(conversation)
    } else if (diff < threeDays) {
      groupMap['一天前'].push(conversation)
    } else if (diff < sevenDays) {
      groupMap['三天前'].push(conversation)
    } else if (diff < thirtyDays) {
      groupMap['七天前'].push(conversation)
    } else {
      groupMap['三十天前'].push(conversation)
    }
  }
  console.log('----groupMap', groupMap)
  return groupMap
}

// ============ 组件 onMounted
const { activeId } = toRefs(props)
</script>

<style lang="scss" scoped>
.conversation-container {
  width: 260px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 0 10px;
  padding-top: 10px;
  .btn-new-conversation {
    padding: 18px 0;
    margin-bottom: 20px;
  }
  .conversation-list {
    .conversation-item {
      margin-top: 5px;
    }
    .classify-title {
      padding-top: 10px;
    }
  }
}





</style>
