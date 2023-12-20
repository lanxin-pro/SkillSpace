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
        :show="show"
        type="bottom"
        :showClose="true"
        round="5"
        @close="show = false"
    >
      <slot name="su-popup"></slot>
    </SuPopup>

  </view>
</template>

<script>
import SuPopup from "@/components/ui/su-popup/su-popup.vue"

export default {
  data() {
    return {
      show: false,
      showClose: false
    }
  },
  props: {
    title: {
      type: String,
      default: ''
    }
  },
  components: {
    SuPopup
  },
  methods: {
    handleClick() {
      // 触发名为 'customEvent' 的事件，可以传递一些数据
      this.show = true
    }
  }
}
</script>

<style lang="scss" scoped>
.content {
  display: flex;
  align-items: center;
  .list-title {
    width: 13%;
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
