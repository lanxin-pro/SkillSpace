<!-- 商品详情：描述卡片 -->
<template>
  <view class="detail-content-card">
    <view class="title">商品详情</view>
    <view class="card-content">
      <mp-html :content="content" />
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      content: '<p><img src="https://static.iocoder.cn/mall/099f261f18576902c4a18209d062a3fd.jpg" alt="" data-href="" style="height: auto;"/><img src="https://static.iocoder.cn/mall/ee1618b3334641c707cadbb9e14a5bae.jpg" alt="" data-href="" style="height: auto;"/><img src="https://static.iocoder.cn/mall/1251259c8968cdce993e47dd121b50a7.jpg" alt="" data-href="" style="height: auto;"/></p>'
    }
  },
  name: "detail-content-card"
}
</script>

<style scoped>
.detail-content-card {
  padding: 20rpx;
  background: #ffffff;
  border-radius: 25rpx;
  .title {
    font-weight: 600;
    font-size: 28rpx;
  }
  .card-content {
    margin-top: 20rpx;
  }
}
</style>
