<template>
  <view>
    <view class="box-nav-bar">
      <uni-nav-bar
          background-color="#ffffff"
          left-icon="left"
          right-icon="cart"
          :title="'购物车(' + number + ')'"
          size="20"
          :border="false"
      >
        <template v-slot:left>
          <text class="iconfont icon-xiangzuo"></text>
        </template>
        <template v-slot:right>
          <view>管理</view>
        </template>
      </uni-nav-bar>
    </view>

    <view>
      内容区域
    </view>

    <view>
      <DetailTabbar />
    </view>
  </view>
</template>

<script setup>
import { ref } from "vue"
import DetailTabbar from './components/detail-tabbar.vue'

const number = ref(3)
</script>

<style scoped lang="scss">

</style>
