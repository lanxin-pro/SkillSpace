<template>
  <view class="tabbar-box-fixed">
    <view class="tabbar-box">
      <view class="tab-barn">
        <view class="left-barn">
          <view class="bottom-help-icon">

          </view>
          <view @click="clickUniNavigateTo" class="bottom-cart-icon">
            <view class="cart-number">
              {{ cartCount }}
            </view>
          </view>
        </view>
        <view class="right-tab-barn">
          <view class="bottom-add" @tap="emits('addCartPopup')">
            加购物车
          </view>
          <view class="pay-in-advance" @tap="emits('onConfirm')">
            <view class="pay-in">支付定金</view>
            <cn-money :money="price" :size="26" color="#fffff" />
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import CnMoney from '@/sheep/components/cn-money/cn-money.vue'

/**
 *
 * 底部导航
 *
 * @property {String} bg 			 			- 背景颜色Class
 * @property {String} ui 			 			- 自定义样式Class
 * @property {Boolean} noFixed 		 			- 是否定位
 * @property {Boolean} topRadius 		 		- 上圆角
 */
const props = defineProps({
  cartCount: {
    type: Number,
    default: 0,
  },
  price: {
    type: Number,
    default: 0,
  }
})

const emits = defineEmits(['addCartPopup','onConfirm'])

const clickUniNavigateTo = () => {
  uni.navigateTo({
    url: '/pages/member_purchases/shopping-trolley/index'
  })
}
</script>

<style scoped lang="scss">
.tabbar-box-fixed {
  box-shadow: 2rpx 2rpx 2rpx 2rpx #ececec;
  border: 1rpx solid #ececec;
  background: #ffffff;
  position: fixed;
  bottom: 0;
  width: 100%;
  z-index: 997;
  .tabbar-box {
    padding: 0 5rpx 0 30rpx;
    .tab-barn {
      display: flex;
      align-items: center;
      gap: 45rpx;
      .left-barn {
        display: flex;
        gap: 32rpx;
        .bottom-help-icon {
          width: 44rpx;
          height: 44rpx;
          background-image: url("@/static/images/member-purchase/project/components/icon-help.1b14df6.svg");
          background-size: cover;
          background-position: center center; /* 设置背景图片位置，居中显示 */
          background-repeat: no-repeat; /* 防止背景图片重复显示 */
        }
        .bottom-cart-icon {
          width: 44rpx;
          height: 44rpx;
          background-image: url("@/static/images/member-purchase/project/components/001.png");
          background-size: cover;
          background-position: center center; /* 设置背景图片位置，居中显示 */
          background-repeat: no-repeat; /* 防止背景图片重复显示 */
          position: relative;
          .cart-number {
            position: absolute;
            top: -13rpx;
            right: -13rpx;
            background: #fa7495;
            border-radius: 14rpx;
            font-size: 20rpx;
            padding: 1rpx 12rpx;
            color: #FFFFFF;
          }
        }
      }
      .right-tab-barn {
        display: flex;
        padding: 20rpx 0;
        gap: 14rpx;
        font-size: 26rpx;
        .bottom-add {
          display: flex;
          justify-content: center;
          align-items: center;
          width: 256rpx;
          height: 72rpx;
          border-radius: 35rpx;
          border: 4rpx solid #fb8ead;
          color: #df4f73;
          font-weight: 500;
        }
        .pay-in-advance {
          padding: 4rpx 0;
          /* 不知道是不是错觉的问题，总感觉两个不一样大 */
          width: 260rpx;
          height: 72rpx;
          border-radius: 35rpx;
          /* 高级的颜色 */
          /*background: linear-gradient(to right, #aaf, #001);*/
          background: linear-gradient(to right, #fc7296, #e54c72);
          color: #ffffff;
          display: flex;
          flex-direction: column;
          align-items: center;
          .pay-in {

          }

        }
      }
    }
  }
}
</style>
