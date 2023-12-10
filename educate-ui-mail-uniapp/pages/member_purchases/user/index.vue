<template>
	<view>
    <view class="box-nav-bar">
      <uni-nav-bar
          background-color="#ffffff"
          left-icon="left"
          right-icon="cart"
          title="会员购中心"
          size="20"
          :border="false"
          @clickLeft="clickLeft"
          @clickRight="clickRight"
      >
        <template slot="left">
          <text class="iconfont icon-xiangzuo"></text>
        </template>
        <template slot="right">
          <img class="right-img" src="../../../static/images/3-002.png" />
        </template>
      </uni-nav-bar>
    </view>
    <div>
      <!-- 头像 -->
      <image class="user-avatar" src="../../../static/images/user-info/noface.jpg" />
    </div>
    
	</view>
</template>

<script>
export default {
  methods: {
    clickLeft(){
      uni.navigateBack({
        delta: 1  // 返回的页面数，如果是1，则返回上一级页面
      });

    },
    clickRight(){
      uni.navigateBack({
        delta: 1  // 返回的页面数，如果是1，则返回上一级页面
      });

    }
  }
}
</script>

<style scoped>
.box-nav-bar {
  .right-img {
    width: 40rpx
  }
}
.user-avatar {
  cursor: pointer;
  width: 55px;
  height: 55px;
  border-radius: 50%;
}
</style>
