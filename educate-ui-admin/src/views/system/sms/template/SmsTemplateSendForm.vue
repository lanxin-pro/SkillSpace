<template>

</template>

<script setup>
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import { DICT_TYPE, getIntDictOptions,getStrDictOptions } from '@/utils/dict.js'
import { getSmsChannel,createSmsChannel,updateSmsChannel } from '@/api/system/sms/channel/index.js'
import { getNotice,createNotice,updateNotice } from '@/api/system/notice/index.js'
import { CommonStatusEnum, SystemMenuTypeEnum } from '@/utils/constants.js'
import ElComponent from '@/plugins/modal.js'
import Editor from '@/components/Editor/index.vue'

</script>

<style scoped>

</style>
