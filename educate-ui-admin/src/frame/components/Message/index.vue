<template>
  <div>
<!--
  placement	出现位置
-->
    <el-popover placement="bottom" width="600" trigger="click">
      <template #reference>
        <!-- icon 展示 小红点 -->
        <el-badge slot="reference" :is-dot="unreadCount > 0" type="danger">
          <el-icon size="22" @click="getList"><Comment /></el-icon>
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
import router from "@/router/index.js";


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

<style scoped>
.el-badge__content.is-fixed {
  top: 100px; /* 保证徽章的位置 */
}
</style>
