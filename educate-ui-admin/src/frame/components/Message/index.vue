<template>
  <div class="message">
    <ElPopover :width="400" placement="bottom" trigger="click">
      <template #reference>
        <ElBadge :is-dot="unreadCount.data > 0" class="item">
          <font-awesome-icon icon="fa-solid fa-comment" @click="getList" />
        </ElBadge>
      </template>
      <ElTabs v-model="activeName">
        <ElTabPane label="我的站内信" name="notice" v-loading="loading">
          <div class="message-list">
            <template v-for="item in list" :key="item.id">
              <div class="message-item">
                <img alt="" class="message-icon" src="@/assets/imgs/avatar.jpg" />
                <div class="message-content">
                  <span class="message-title">
                    {{ item.templateNickname }}：{{ item.templateContent }}
                  </span>
                  <span class="message-date">
                    {{ formatDate(item.createTime) }}
                  </span>
                </div>
              </div>
            </template>
          </div>
        </ElTabPane>
      </ElTabs>
      <!-- 更多 -->
      <div style="margin-top: 10px; text-align: right">
        <el-button icon="view" type="primary" @click="goMyList">
          查看全部
        </el-button>
      </div>
    </ElPopover>
  </div>
</template>

<script setup>
import {ref,onMounted,getCurrentInstance} from 'vue'
import { getUnreadNotifyMessageList,getUnreadNotifyMessageCount } from '@/api/system/notify/message/index.js'
import { useRouter } from 'vue-router'
import { formatDate } from '@/utils/formatTime.js'

const { push } = useRouter()
const activeName = ref('notice')
// 未读消息数量
const unreadCount = ref(0)
// 消息列表
const list = ref([])
const loading = ref(true)

// 获得消息列表
const getList = async () => {
  const response = await getUnreadNotifyMessageList()
  list.value = response.data
  // 强制设置 unreadCount 为 0，避免小红点因为轮询太慢，不消除
  // unreadCount.value = 0
}

// 获得未读消息数
const getUnreadCount = async () => {
  loading.value = true
  getUnreadNotifyMessageCount().then((data) => {
    unreadCount.value = data
  }).finally(
    loading.value = false
  )
}

// 跳转我的站内信
const goMyList = () => {
  push({
    // push路由
    name: 'MyNotifyMessage'
  })
}

// ========== 初始化 =========
onMounted(() => {
  // 首次加载小红点
  getUnreadCount()
  // 轮询刷新小红点
  setInterval(
      () => {
        getUnreadCount()
      },
      1000 * 60 * 2
  )
})

</script>

<style lang="scss">
.message-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 260px;
  line-height: 45px;
}

.message-list {
  display: flex;
  flex-direction: column;
  .message-item {
    display: flex;
    align-items: center;
    padding: 20px 0;
    border-bottom: 1px solid var(--el-border-color-light);

    &:last-child {
       border: none;
    }

    .message-icon {
      width: 40px;
      height: 40px;
      margin: 0 20px 0 5px;
    }

    .message-content {
      display: flex;
      flex-direction: column;

      .message-title {
        margin-bottom: 5px;
      }

      .message-date {
        font-size: 12px;
        color: var(--el-text-color-secondary);
      }
    }
  }
}
/* scope是作用域。但是我想要在全局生效这个配置 */
.el-badge__content.is-fixed {
  top: 10px; /* 保证徽章的位置 */
}
</style>
