<template>
  <view>
    <DetailCell title="预售流程">
      <template slot="middle-content">
        <view class="procedure-items">

          <view class="pro-item active">

            <view class="pro-item-top">
              <view class="top-time">
                2023.09.06
              </view>
            </view>

            <view class="progress-dot">
              <!-- 中间:after -->
            </view>

            <view class="pro-item-bottom">
              预售开始
            </view>

          </view>

          <view class="pro-item">
            <view class="pro-item-top">
              <view class="top-time">
                2024.04.03
              </view>
            </view>
            <view class="progress-dot">
              <!-- 中间:after -->
            </view>
            <view class="pro-item-bottom">
              制作完成
            </view>
          </view>

          <view class="pro-item">
            <view class="pro-item-top">
              <view class="top-time">
                2024.04.08
              </view>
            </view>
            <view class="progress-dot">
              <!-- 中间:after -->
            </view>
            <view class="pro-item-bottom">
              到货补款
            </view>
          </view>

          <div class="progress-box"></div>
        </view>
      </template>

      <template slot="su-popup">
        <view class="attention-details">
          <view class="about-freight">
            <view class="title">关于运费</view>
            <text class="details display-inline-block">
              单笔订单中合并发货的商品实付总金额（含定金+尾款）满足包邮门槛即可包邮，定金预售商品的运费将在补款发货时产生。
              具体物流规则以商品页面展示为准。
            </text>

          </view>
          <view class="about-freight">
            <view class="title">不支持7天无理由</view>
            <text class="details">特殊商品及类目不支持7天无理由退货。如，手办模玩类均不支持7天无理由（手办为手工制品，难免会有瑕疵点，敬请谅解）</text>
          </view>

          <view class="about-freight">
            <view class="title">保价保单</view>
            <text class="details">保证补款时全款价格不会高于预售价格。预售商品在补款后保证发货（供应商不供货、取消制作除外，出现这类情况无条件返还定金）。</text>
          </view>

        </view>
      </template>
    </DetailCell>
  </view>
</template>

<script>
import DetailCell from "./detail-cell"

export default {
  data() {
    return {

    }
  },
  components: {
    DetailCell
  },
}
</script>

<style scoped lang="scss">
.procedure-items {
  align-items: center;
  overflow: scroll;
  width: 100%;
  display: flex;
  justify-content: space-between;
  flex: 1 1;
  position: relative;
  font-size: 24rpx;
  margin: 5rpx 0rpx 20rpx;
  .pro-item {
    display: flex;
    flex-direction: column;
    gap: 20rpx;
    align-items: center;
    position: relative;
    padding: 0 20rpx;
    .pro-item-top {
      justify-content: flex-start;
      .top-time {

      }
    }
    .progress-dot {
      width: 10rpx;
      height: 10rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #bfbfbf;
      position: absolute;
      left: 50%;
      top: 50%;
      margin: -4rpx 2rpx 0 -4rpx;
      z-index: 10;
    }
    .pro-item-bottom {
      font-weight: 600;
      font-size: 20rpx;
    }
  }
  .pro-item.active {
    color: #fb7299;
    .progress-dot {
      background: #fc7499;
    }
  }
  .progress-box {
    display: block;
    position: absolute;
    left: 0;
    right: 0;
    top: 50%;
    height: 2px;
    margin-top: -1px;
    background: #f4f4f4;
    border-radius: 2px;
    z-index: 1;
  }
}

</style>


