<template>
  <div class="execution" v-for="item in props.list" :key="item.id">
    <div
        class="avue-comment"
        :class="{ 'avue-comment--reverse': item.sendFrom === SendFrom.mpBot }"
    >
      <div class="avatar-div">
        <img :src="getAvatar(item.sendFrom)" class="avue-comment__avatar" />
        <div class="avue-comment__author">
          {{ getNickname(item.sendFrom) }}
        </div>
      </div>
      <div class="avue-comment__main">
        <div class="avue-comment__header">
          <div class="avue-comment__create_time">{{ formatDate(item.createTime) }}</div>
        </div>
        <div
            class="avue-comment__body"
            :style="item.sendFrom === SendFrom.mpBot ? 'background: #6BED72;' : ''"
        >
          <Msg :item="item" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { propTypes } from '@/utils/propTypes.js'
import { reactive } from 'vue'
import avatarWechat from '@/assets/imgs/wechat.png'
import { formatDate } from '@/utils/formatTime.js'
import Msg from './Msg.vue'

const props = defineProps({
  list: propTypes.any,
  accountId: propTypes.number,
  user: propTypes.string
})
const SendFrom = reactive({
  user: 1,
  mpBot: 2
})

const getAvatar = (sendFrom) =>
    sendFrom === SendFrom.user ? props.user.avatar : avatarWechat

const getNickname = (sendFrom) =>
    sendFrom === SendFrom.user ? props.user.nickname : '公众号'
</script>

<style lang="scss">
/* 因为 joolun 实现依赖 avue 组件，该页面使用了 comment.scss、card.scc  */
@import url('../comment.scss');
@import url('../card.scss');

.avatar-div {
  width: 80px;
  text-align: center;
}
</style>
