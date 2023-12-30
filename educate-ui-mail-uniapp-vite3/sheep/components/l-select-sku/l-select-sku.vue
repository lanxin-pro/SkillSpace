<!-- 规格与数量弹框 -->
<template>
  <view class="app-container">
    <!-- 规格弹窗 -->
    <su-popup :showClose="true" :show="show" round="10" @close="emits('close')">
      <view class="popup-container">
        <!-- SKU 信息 -->
        <view class="header-shopping-info">
          <view class="img-info">
            <img src="https://file.sheepjs.com/storage/test/20220821/8d59f6f7d9676134796b899969049c12.jpg">
          </view>
          <cn-money :money="16.35" :size="36" color="#ee719b" />
        </view>
        <view class="shopping-sku-specification">
          <view class="title">
            更多系列
          </view>
          <view class="sku-container">
            <view class="sku-item hover">喜茶一周年茶1盒</view>
            <view class="sku-item">喜茶一周年茶2盒</view>
          </view>
        </view>

        <view>
          <view class="confirm-add-to-shopping-cart">确定</view>
        </view>
      </view>
    </su-popup>
  </view>
</template>

<script setup>
import SuPopup from "@/sheep/ui/su-popup/su-popup"
import CnMoney from '@/sheep/components/cn-money/cn-money.vue'

const emits = defineEmits(['change', 'addCart', 'buy', 'close'])
const props = defineProps({
  goodsInfo: {
    type: Object,
    default () {},
  },
  show: {
    type: Boolean,
    default: false,
  }
});

</script>

<style scoped lang="scss">
.app-container {
  .popup-container {
    min-height: 900rpx;
    display: flex;
    flex-direction: column;
    .header-shopping-info {
      display: flex;
      .img-info {
        margin-right: 20rpx;
        background: #f6f6f6;
        img {
          width: 200rpx;
          background: #f6f7fb;
          border-radius: 20rpx;
        }
      }

    }
    .shopping-sku-specification {
      margin: 30rpx 10rpx;
      flex: 1;
      .title {
        font-weight: 600;
        font-size: 22rpx;
        margin: 20rpx 10rpx;
      }
      .sku-container{
        display: flex;
        gap: 20rpx;
        .sku-item {
          font-size: 20rpx;
          background: #f6f6f6;
          padding: 10rpx 20rpx;
          border-radius: 30rpx;
        }
        .sku-item.hover {
          background: #fef4f3;
          border: 1rpx solid #b9301b;
          color: #b54f46;
        }
      }

    }
    .confirm-add-to-shopping-cart {
      width: 100%;
      background: linear-gradient(to right, #f33f4c, #f43940, #f9332a, #fb2e1b);
      height: 60rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff7f0;
      font-size: 26rpx;
      border-radius: 30rpx;
    }
  }

}
</style>
