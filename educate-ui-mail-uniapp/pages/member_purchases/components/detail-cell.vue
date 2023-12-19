<template>
  <view class="content">
    <view class="list-wrap">
      <view class="logo"></view>
      <view class="tag" :class="1 === 1 ? 'none-tag-icon' : ''">专业包装</view>
      <view class="tag">满129包邮</view>
      <view class="tag">48h内发货</view>
      <view class="tag">延期发货</view>
      <view class="tag">低价保单</view>
      <view class="tag tag-bottom">不支持7天无  理由退货</view>
    </view>
    <view class="right-icon">
      <text style="font-size: 26rpx" class="iconfont icon-xiangyou"></text>
    </view>
  </view>
</template>

<script>
export default {
  name: "detail-cell"
}
</script>

<style lang="scss" scoped>
.content {
  display: flex;
  align-items: center;
  .list-wrap {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    .logo {
      width: 196rpx;
      height: 28rpx;
      background: #fff url(http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=5LiL6L29LnBuZw==&version_id=null) no-repeat 0;
      background-size: 196rpx auto;
      padding: 8rpx 40rpx 8rpx 0;
      position: relative;
      z-index: 2;
      margin-right: 46rpx;
    }
    .tag {
      margin-right: 34rpx;
      padding: 8rpx 0;
      font-size: 24rpx;
      color: #212121;
      line-height: 28rpx;
      position: relative;
    }
    .tag:after {
      content: "";
      display: block;
      width: 4rpx;
      height: 4rpx;
      background: #d3d3d3;
      position: absolute;
      left: -20rpx;
      top: 50%;
      margin-top: -2rpx;
      z-index: 1;
      border-radius: 50%;
    }
    .none-tag-icon:after {
      display: none;
    }
  }
  .right-icon {
    color: #cbcbcb;
    font-weight: 600;
  }
}
</style>
