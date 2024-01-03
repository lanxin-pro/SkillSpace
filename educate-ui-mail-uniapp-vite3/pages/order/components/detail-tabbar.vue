<template>
  <view class="tabbar-box-fixed">
    <view class="agreement">
      <view>
        <uv-checkbox-group  v-model="agreement">
          <uv-checkbox size="12" :name="true" activeColor="#f55b91" />
          同意
        </uv-checkbox-group>
      </view>
      <view class="protocol">
        <navigator url="/pages/users/agreement/index" style="color: #fc7296;">
         《预售商品支付&电商平台用户服务协议》
        </navigator>
      </view>

    </view>
    <view class="tabbar-box">
      <view class="summation">
        <cn-money :money="fen2yuan(totalMoney)" :size="48" color="#ee719b" />
      </view>
      <view class="bottom-add" @tap="emit('onConfirm')">
        <view>
          提交订单
        </view>
      </view>
    </view>

  </view>
</template>

<script setup>
import CnMoney from '@/sheep/components/cn-money/cn-money.vue'
import { fen2yuan } from '@/sheep/hooks/useGoods';
import { ref, reactive, computed } from 'vue'

const props = defineProps({
  totalMoney: {
    type: Number,
    default: 5384,
  },
  totalNumber: {
    type: Number,
    default: 0,
  }
})
const emit = defineEmits(['onConfirm'])
const tempOnSelectAll = ref(true)
const state = reactive({
  editMode: false,
  selectedList: [],
});
// 全选
const onSelectAll = (check) => {
  console.log(check)
}
</script>

<style scoped lang="scss">
.tabbar-box-fixed {
  box-shadow: 2 rpx 2 rpx 2 rpx 2 rpx #ececec;
  border: 1 rpx solid #ececec;
  background: #ffffff;
  position: fixed;
  bottom: 0;
  width: 100%;
  z-index: 999;
  .agreement {
    padding: 20rpx 20rpx 10rpx;
    display: flex;
    align-items: center;
    font-size: 22rpx;
    .protocol {
    }
  }
  .tabbar-box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20rpx 20rpx;
      .summation {
        margin-right: 20rpx;
      }
      .bottom-add {
        /* 不知道是不是错觉的问题，总感觉两个不一样大 */
        width: 180rpx;
        height: 72rpx;
        border-radius: 35rpx;
        /* 高级的颜色 */
        /*background: linear-gradient(to right, #aaf, #001);*/
        background: linear-gradient(to right, #fc7296, #e54c72);
        color: #ffffff;
        display: flex;
        align-items: center;
        justify-content: center;
    }
  }
}
</style>
