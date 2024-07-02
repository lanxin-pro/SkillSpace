<template>
  <view>
    <view class="box-nav-bar">
      <uni-nav-bar
          background-color="#ffffff"
          left-icon="left"
          right-icon="cart"
          :title="'购物车(' + state.list.length + ')'"
          size="20"
          :border="false"
      >
        <template v-slot:left>
          <text @click="clickLift" class="iconfont icon-xiangzuo"></text>
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
      <view class="area" v-for="item of state.list" :key="item.id">
        <view class="pre-sale-area">
          <uv-checkbox-group :value="state.selectedIds" @change="onSelectSingle(item.id)">
            <uv-checkbox :name="item.id" shape="circle" activeColor="#f55b91"></uv-checkbox>
          </uv-checkbox-group>
        </view>
        <l-goods-item
            :title="item.spu.name"
            :img="item.spu.picUrl || item.goods.image"
            :price="item.sku.price"
            :skuText="handleSkuText(item)"
        >
          <template v-slot:tool>
            <su-number-box :min="1" :max="item.sku.stock" :step="1" v-model="item.count"
                           @change="onNumberChange($event, item)"></su-number-box>
          </template>
        </l-goods-item>
      </view>
    </view>

    <view>
      <DetailTabbar
          :total-money="state.totalPriceSelected"
          :total-number="state.selectedIds?.length"
          @onConfirm="handleOnConfirm"
      />
    </view>
  </view>
</template>

<script setup>
import sheep from '@/sheep'
import DetailTabbar from './components/detail-tabbar.vue'
import LGoodsItem from '@/sheep/components/l-goods-item/l-goods-item.vue'
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

const cart = sheep.$store('cart')

const number = ref(3)
const state = reactive({
  editMode: false,
  list: computed(() => cart.list),
  selectedList: [],
  selectedIds: computed(() => cart.selectedIds),
  isAllSelected: computed(() => cart.isAllSelected),
  totalPriceSelected: computed(() => cart.totalPriceSelected),
})
/* 结算 */
const handleOnConfirm = () => {
  uni.navigateTo({
    url: '/pages/order/confirm'
  })
}

/* 值的变化 */
const handleSkuText = (item) => {
  console.log(item)
  return item.sku.properties.length > 1
      ?
      /* TODO j-sentinel 这里好像是累加算的目前没有验证 这里我觉得就会有问题，如果有3个property会怎么样？ */
      // reduce 数组中每个元素valueName 执行累加操作后的最终值
      item.sku.properties.reduce( (items, items2) => {
        return items.valueName + ' ' + items2.valueName
      })
      :
      item.sku.properties[0].valueName
}

/* 单选选中 */
const onSelectSingle = (id) => {
  console.log('单选', id)
  cart.selectSingle(id)
}
/* 商品数量发生变化 */
const onNumberChange = () => {

}
/* 返回上一页 */
const clickLift = () => {
  uni.navigateBack({
    delta: 1  // 返回的页面数，如果是1，则返回上一级页面
  })
}
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

  }

}
</style>
