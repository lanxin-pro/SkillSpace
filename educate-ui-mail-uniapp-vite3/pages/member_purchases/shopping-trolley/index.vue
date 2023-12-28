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
          <view class="right-hand-management">管理</view>
        </template>
      </uni-nav-bar>
    </view>

    <view class="content-area">
<!--      <view class="domestic-delivery">
        国内到货
      </view>-->
      <view class="area" v-for="item of 2">
      <view class="pre-sale-area">
        <uv-checkbox-group>
          <uv-checkbox name="1" shape="circle" activeColor="#f55b91" ></uv-checkbox>
        </uv-checkbox-group>
      </view>
      <view class="pre-details-sale-area">
        <view class="details-imgage">
          <img src="http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=YmI3YmY0NmFjYmRiZjcwMzlmMDExODJlYzI1Yzg3YmEucG5n&version_id=null" />
        </view>
        <view class="pre-details">

          <view class="describe-title display-inline">
            <view class="presale-sign">
              预售
            </view>
            AniMester大漫匠 少女前线：云图计划 芙洛伦 药心巧克力Ver. 1/7 比例手办比例手办
          </view>
          <view class="quantity-operation">
            <cn-money :money="16.35" :size="36" color="#ee719b" />
            <view>
              <su-number-box :min="1" :max="6" :step="1" v-model="number"></su-number-box>
            </view>
          </view>
        </view>
      </view>
    </view>
    </view>

    <view>
      <DetailTabbar />
    </view>
  </view>
</template>

<script setup>
import sheep from '@/sheep'
import DetailTabbar from './components/detail-tabbar.vue'
import CnMoney from '@/sheep/components/cn-money/cn-money.vue'
import SuNumberBox from '@/sheep/ui/su-number-box/su-number-box.vue'
import {
  onLoad,
  onPageScroll
} from '@dcloudio/uni-app'
import {
  ref,
  reactive,
  computed,
  watch
} from 'vue'

const number = ref(3)

onLoad(()=>{
  const cart = sheep.$store('cart')
  console.log(cart.list)
})

</script>

<style scoped lang="scss">
.box-nav-bar {
  .right-hand-management {
    font-size: 28rpx;
  }
}

.content-area {
  background: #ffffff;
  .domestic-delivery {
    font-size: 26rpx;
    font-weight: 600;
  }
  .area {
    display: flex;
    align-items: center;
    padding: 10rpx 30rpx;
    .pre-sale-area {
      margin-right: 20rpx;
    }
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
        .quantity-operation {
          display: flex;
          justify-content: space-between;
          align-items: center;
        }
      }
    }
  }

}
</style>
