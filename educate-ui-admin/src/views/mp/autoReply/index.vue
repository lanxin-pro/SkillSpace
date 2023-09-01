<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        class="-mb-15px"
        :model="queryParams"
        :inline="true"
        label-width="68px"
        size="small"
    >
      <el-form-item label="公众号" prop="accountId">
        <WxAccountSelect @change="onAccountChanged" />
      </el-form-item>
    </el-form>



  </div>
</template>

<script setup>
import { getAccountPage,generateAccountQrCode,clearAccountQuota,deleteAccount } from '@/api/mp/account/index.js'
import Pagination from '@/components/Pagination/index.vue'
import { dateFormatter,formatDate } from '@/utils/formatTime'
import { fileSizeFormatter } from '@/utils'
import ELComponent from '@/plugins/modal.js'
import { ref, reactive,onMounted } from 'vue'
import WxAccountSelect from './component/WxAccountSelect.vue'

/** 侦听账号变化 */
const onAccountChanged = (id,name) => {

}
</script>

<style scoped>

</style>
