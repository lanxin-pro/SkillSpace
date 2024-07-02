<template>
  <view>
    <view class="pre-details-sale-area">
      <view class="details-imgage">
<!--        <img src="http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=YmI3YmY0NmFjYmRiZjcwMzlmMDExODJlYzI1Yzg3YmEucG5n&version_id=null" />-->
        <img :src="img" />
      </view>
      <view class="pre-details">

        <view class="describe-title display-inline">
          <view class="presale-sign">
            预售
          </view>
          <text class="ml10" v-if="title">
            {{ title }}
          </text>
        </view>
        <view class="property-describe" v-if="skuString">
          {{ skuString }}
        </view>
        <view class="quantity-operation">
          <cn-money :money="fen2yuan(price)" :size="36" color="#ee719b" />
          <view>
            <slot name="tool"></slot>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
  import CnMoney from '@/sheep/components/cn-money/cn-money.vue'
  import sheep from '@/sheep';
  import { computed } from 'vue';
  import { fen2yuan } from '@/sheep/hooks/useGoods'
  /**
   * 订单卡片
   *
   * @property {String} img 											- 图片
   * @property {String} title 										- 标题
   * @property {Number} titleWidth = 0								- 标题宽度，默认0，单位rpx
   * @property {String} skuText 										- 规格
   * @property {String | Number} price 								- 价格
   * @property {String} priceColor 									- 价格颜色
   * @property {Number | String} num									- 数量
   *
   */
  const props = defineProps({
    img: {
      type: String,
      default: 'https://img1.baidu.com/it/u=1601695551,235775011&fm=26&fmt=auto',
    },
    title: {
      type: String,
      default: '',
    },
    titleWidth: {
      type: Number,
      default: 0,
    },
    skuText: {
      type: [String, Array],
      default: '',
    },
    price: {
      type: [String, Number],
      default: '',
    },
    priceColor: {
      type: [String],
      default: '#ee719b',
    },
    num: {
      type: [String, Number],
      default: 0,
    },
    score: {
      type: [String, Number],
      default: '',
    },
    radius: {
      type: [String],
      default: '',
    },
    marginBottom: {
      type: [String],
      default: '',
    },
  });
  const skuString = computed(() => {
    if (!props.skuText) {
      return '';
    }
    if (typeof props.skuText === 'object') {
      return props.skuText.join(',');
    }
    return props.skuText;
  });
</script>

<style lang="scss" scoped>
.pre-details-sale-area {
  display: flex;
  .details-imgage {
    img {
      width: 168rpx;
      height: 168rpx;
      cursor: pointer;
      border-radius: 6rpx;
      background: #f6f7fb
    }
  }
  .pre-details {
    margin-left: 16rpx;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    .describe-title {
      margin-left: 4rpx;
      font-size: 24rpx;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      overflow: hidden;
      -webkit-line-clamp: 2; /* 显示的行数 */
      text-overflow: ellipsis;
      color: #161616;
      .presale-sign {
        font-size: 18rpx;
        fill: #505050;
        color: #4e4d53;
        padding: 4rpx 6rpx;
        background: #eeeeee;
        border-radius: 6rpx;
        display: inline-block;
      }
    }
    .property-describe {
      font-size: 22rpx;
      color: #999999;
    }
    .quantity-operation {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 440rpx;
    }
  }
}
</style>
