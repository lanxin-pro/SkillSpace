<template>
  <div>
<!--
  placement	出现位置
-->
    <el-popover
        placement="bottom"
        width="600"
        trigger="click"
    >
      <template #reference>
        <!-- icon 展示 小红点 -->
        <el-badge :is-dot="unreadCount > 0" type="danger">
          <SvgIcon
              style="vertical-align: text-top"
              icon-class="fa-comment-dots"
          />

        </el-badge>
      </template>

        <!-- 弹出列表 -->
        <el-table v-loading="loading" :data="list">
          <el-table-column width="120" property="templateNickname" label="发送人" />
          <el-table-column width="180" property="createTime" label="发送时间">
            <template v-slot="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="类型" align="center" prop="templateType" width="100">
            <template v-slot="scope">
              <dict-tag :type="DICT_TYPE.SYSTEM_NOTIFY_TEMPLATE_TYPE" :value="scope.row.templateType" />
            </template>
          </el-table-column>
          <el-table-column property="templateContent" label="内容" />
        </el-table>

        <!-- 更多 -->
        <div style="text-align: right; margin-top: 10px">
          <el-button type="primary" size="mini" @click="goMyList">查看全部</el-button>
        </div>


    </el-popover>
  </div>
</template>

<script setup>
import {ref,onMounted,getCurrentInstance} from 'vue'
import { parseTime } from '@/utils/ruoyi.js'
import router from "@/router/index.js"
import SvgIcon from '@/components/SvgIcon/index.vue'

// 遮罩层
const loading = ref(false)
// 列表
const list = ref([])
// 未读数量,
const unreadCount = ref(0)

onMounted(()=>{
  getUnreadCount()
})


// 从后台中查询
const getUnreadCount = (()=>{
    unreadCount.value = 1
})
const goMyList = (()=>{
  router.push({
    name: 'MyNotifyMessage'
  });
})
</script>

<style>
/* scope是作用域。但是我想要在全局生效这个配置 */
.el-badge__content.is-fixed {
  top: 10px; /* 保证徽章的位置 */
}
</style>
