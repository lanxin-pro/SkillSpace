<template>
  <view class="tabbar-box-fixed">
    <view class="tabbar-box">
      <view class="footer-left">
        <uv-checkbox-group @change="onSelectAll" v-model="tempOnSelectAll">
          <uv-checkbox name="全选" shape="circle" activeColor="#f55b91" label="全选"></uv-checkbox>
        </uv-checkbox-group>
      </view>
      <view class="footer-right">
        <view class="summation">
          <text style="margin-right: -10rpx;font-size: 26rpx;">合计：</text>
          <cn-money :money="16.35" :size="28" color="#ee719b" />
        </view>
        <view class="bottom-add">
          <view>
            结算(0)
          </view>
        </view>
      </view>
    </view>

  </view>
</template>

<script setup>
import CnMoney from '@/sheep/components/cn-money/cn-money.vue'
import { ref, reactive, computed } from 'vue'

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
  .tabbar-box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx 20rpx;
    .footer-left {
      display: flex;
      align-items: center;
    }
    .footer-right {
      display: flex;
      justify-content: space-between;
      align-items: center;
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

}
</style>
