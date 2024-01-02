<template>
  <view class="container">
    <view class="box-nav-bar">
        <uni-nav-bar
            background-color="#ffffff"
            left-icon="left"
            right-icon="cart"
            title="确认订单"
            size="20"
            :border="false"
        >
          <template v-slot:left>
            <text @click="clickLift" class="iconfont icon-xiangzuo"></text>
          </template>
          <template v-slot:right>
            <view></view>
          </template>
        </uni-nav-bar>
      </view>


    <view class="address-box">
      <DetailCell>
        <template v-slot:middle-content>
            <view class="address-box-header">
              <view class="address-tag">默认</view>
              <view class="address-name">
                陕西省 西安市 雁塔区
              </view>
            </view>
            <view class="address-text">
              雁南四路与芙蓉西路交口西 曲江南苑
            </view>
            <view>
              董伟豪 13571082448
            </view>
        </template>
        <template v-slot:su-popup>
          <view class="attention-details">
            收获地址的更改
          </view>
        </template>
      </DetailCell>
    </view>


    <view class="spot-commodity">
      <view class="spot-title">
        现货商品
      </view>
      <view class="commodity-area">
        <view class="left-img">
          <img src="http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=YmI3YmY0NmFjYmRiZjcwMzlmMDExODJlYzI1Yzg3YmEucG5n&version_id=null" />
        </view>
        <view class="right-detailed">
          <view class="detailed-information">
            <view>
              <text class="presell-sign">
                现货
              </text>
              <text class="detailed-title">
                <!--      TODO j-sentinel 这里应该对文字的长度进行优化      -->
                FuRyu TENITOL 想要成为影之实力者！ 贝塔 手办
              </text>
            </view>
            <view class="specification">
              规格：浅色
            </view>
            <view>
              <cn-money
                  :money="fen2yuan(200)"
                  :size="32"
                  color="#191919" />
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="delivery-container">
      <view class="preferential-delivery">
        <view class="item-title">
          优惠劵
        </view>
        <view class="right-coupon">
          <view class="coupon-information">
            新人礼包5元无门槛劵 -￥5.00
          </view>
          <text class="iconfont icon-xiangyou"></text>
        </view>
      </view>
      <view class="delivery">
        <view class="left-title">配送</view>
        <view class="right-express-information">
          <view>
            快递<text class="color-pink">￥10.00</text>
          </view>
          <view>23:59前付款，预计01月19日送达</view>
        </view>
      </view>
    </view>


  </view>
</template>

<script setup>
import CnMoney from '@/sheep/components/cn-money/cn-money.vue'
import { fen2yuan } from '@/sheep/hooks/useGoods'
import DetailCell from "@/pages/member_purchases/goods_details/components/detail-cell.vue"

/* 返回上一页 */
const clickLift = () => {
  uni.navigateBack({
    delta: 1  // 返回的页面数，如果是1，则返回上一级页面
  })
}
</script>

<style scoped lang="scss">
.address-box {
  background: #FFFFFF;
  padding: 20rpx;
  font-size: 24rpx;
  .address-box-header {
    display: flex;
    align-items: center;
    .address-tag {
      color: #d96990;
      border: 1rpx solid #d96990;
      border-radius: 6rpx;
      padding: 2rpx 4rpx;
      margin-right: 10rpx;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .address-name {
    }
  }
  .address-text {
    background: #FFFFFF;
    font-weight: 600;
    font-size: 28rpx;
    margin: 2rpx 0 12rpx;
  }
}
.spot-commodity {
  margin-top: 20rpx;
  background: #FFFFFF;
  padding: 20rpx;
  font-size: 24rpx;
  .spot-title {
  }
  .commodity-area {
    display: flex;
    margin-top: 16rpx;

    .left-img {
      background: #f2f3f5;
      border-radius: 10rpx;
      img {
        width: 160rpx;
        height: 160rpx;
        /* 只显示一个图片， */
        cursor: pointer;
        /* 图片比例缩放 */
        /* 使用 cover 填充整个容器，可能裁剪部分图片 */
        object-fit: cover;
      }
    }
    .right-detailed {
      margin-left: 16rpx;
      .detailed-information {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        height: 100%;
        .presell-sign {
          font-size: 18rpx;
          fill: #505050;
          color: #4e4d53;
          padding: 4rpx 6rpx;
          background: #eeeeee;
          border-radius: 6rpx;
          display: inline-block;

        }
        .detailed-title {
          overflow: hidden;
        }
      }
      .specification {
        font-size: 20rpx;
        color: #8c8c8c;
      }

    }

  }
}

.delivery-container {
  background: #FFFFFF;
  margin-top: 20rpx;
  padding: 20rpx;
  font-size: 24rpx;
  width: 100%;
  .preferential-delivery {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10rpx 0;
    .item-title {

    }
    .right-coupon {
      display: flex;
      align-items: center;
      .coupon-information {
        display: inline-block;
        margin-right: 10rpx;
        font-weight: 600;
      }
      .iconfont {
        color: #cbcbcb;
        font-weight: 600;
        right: 0;
        font-size: 22rpx
      }
    }


  }
  .delivery {
    margin-top: 26rpx;
    display: flex;
    justify-content: space-between;
    border-top: 1rpx solid #e5e5e5; /* 上方边框，1px粗细，黑色 */
    border-bottom: 1rpx solid #e5e5e5; /* 下方边框，1px粗细，黑色 */
    padding: 20rpx 0;
    .left-title {

    }
    .right-express-information {
      display: flex;
      flex-direction: column;
      align-items: end;
      gap: 20rpx;
    }
  }
}
</style>
