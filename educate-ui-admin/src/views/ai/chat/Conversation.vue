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
        <el-empty v-if="true" v-loading="true" description=""/>
        <div v-for="conversationKey in Object.keys(conversationMap)" :key="conversationKey">
          <div
              v-if="conversationMap[conversationKey].length"
              class="conversation-item classify-title"
          >
            <el-text class="mx-1" size="small" tag="b">{{ conversationKey }}</el-text>
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
const conversationMap = ref({"a": [1,2,3],"b": [4,5,6],"c": [7,8,9]})
// 对话列表
const conversationList = ref([])
// 选中的对话，默认为 null
const activeConversationId = ref(null)
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
}

onMounted(async () => {
  // 获取对话 列表
  await getChatConversationList()
})

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
}
.btn-new-conversation {
  padding: 18px 0;
  margin-bottom: 20px;
}
.conversation-list {

}

.conversation-item {
  margin-top: 5px;
}
.classify-title {
  padding-top: 10px;
}
</style>
