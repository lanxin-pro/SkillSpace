<template>

</template>

<script setup>
import { ref,reactive,onMounted,nextTick } from 'vue'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { deleteSmsChannel,getSmsChannelPage } from '@/api/system/sms/channel/index.js'
import { handleTree } from '@/utils/ruoyi.js'
import SvgIcon from '@/components/SvgIcon/index.vue'
import { SystemMenuTypeEnum, CommonStatusEnum } from '@/utils/constants'
import DictTag from '@/components/DictTag/index.vue'
import ELComponent from '@/plugins/modal.js'
import { dateFormatter } from '@/utils/formatTime.js'
import Pagination from '@/components/Pagination/index.vue'


</script>

<style scoped>

</style>
