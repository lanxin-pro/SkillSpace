<!--
          插槽slot的name
 middle-content 中间的内容
 su-popup 弹窗

 -->
<template>
  <view class="content">
    <view :class="title != '' ? 'list-title' : ''">
      {{ title }}
    </view>
    <view class="list-wrap" @click="handleClick">
      <slot name="middle-content"></slot>
    </view>

    <view class="right-icon">
      <text style="font-size: 26rpx" class="iconfont icon-xiangyou"></text>
    </view>

    <SuPopup
        :title="propsTitle"
        :show="show"
        type="bottom"
        :showClose="showClose"
        round="5"
        @close="show = false"
    >
      <slot name="su-popup"></slot>
    </SuPopup>

  </view>
</template>

<script setup>
import SuPopup from "@/sheep/ui/su-popup/su-popup.vue"
import { ref, reactive } from 'vue'

const show = ref(false)
const showClose = ref(true)
const props = defineProps({
  title: {
    type: String,
    default: ''
  },
  propsTitle: {
    type: String,
    default: ''
  }
})
const handleClick = ()=> {
  // 触发名为 'customEvent' 的事件，可以传递一些数据
  show.value = true
}

</script>

<style lang="scss" scoped>
.content {
  display: flex;
  align-items: center;
  .list-title {
    width: 10%;
    font-size: 24rpx;
    color: #9d9d9d;
  }
  .list-wrap {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    //width: 896rpx;
    min-width: 80%;
    margin-right: 40rpx;
  }
  .right-icon {
    color: #cbcbcb;
    font-weight: 600;
    right: 0;
  }
}
</style>
