<template>
  <view>
    <DetailCell title="参数">
      <template slot="middle-content">
        <view class="content">
          <view class="item-complex">
            <view class="item-complex-key">
              尺寸
            </view>
            <view class="item-complex-value">
              高约240mm
            </view>
          </view>

          <view class="item-complex">
            <view class="item-complex-key">
              比例
            </view>
            <view class="item-complex-value">
              无比例
            </view>
          </view>

          <view class="item-complex">
            <view class="item-complex-key">
              发售日
            </view>
            <view class="item-complex-value">
              2024-05
            </view>
          </view>

          <view class="item-complex">
            <view class="item-complex-key">
              材质
            </view>
            <view class="item-complex-value">
              PVC
            </view>
          </view>

          <view class="item-complex">
            <view class="item-complex-key">
              材质
            </view>
            <view class="item-complex-value">
              PVC
            </view>
          </view>

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
  components: {
    DetailCell
  },
}
</script>

<style scoped lang="scss">
.content {
  height: 80rpx;
  overflow: scroll;
  display: flex;
  .item-complex {
    position: relative;
    padding: 0 20rpx;
    .item-complex-key {
      text-align: center;
      width: 100%;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      font-size: 22rpx;
      line-height: 28rpx;
      color: #999;
    }
    .item-complex-value {
      text-align: center;
      width: 100%;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      font-size: 26rpx;
      line-height: 32rpx;
      color: #212121;
      margin-top: 12rpx;
    }
  }
  .item-complex:first-child:after {
    width: 0;
  }
  .item-complex:after {
    content: "";
    display: block;
    position: absolute;
    left: 0;
    top: 0;
    height: 100%;
    width: 2rpx;
    background: #d9d9d9;
    -webkit-transform: scaleX(.5);
    transform: scaleX(.5);
    -webkit-transform-origin: left top;
    transform-origin: left top;
  }
}
</style>
