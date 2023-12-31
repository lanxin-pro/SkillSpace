<!-- 规格与数量弹框 -->
<template>
  <view class="app-container">
    <!-- 规格弹窗 -->
    <su-popup z-index="998" :showClose="true" :show="show" round="10" @close="emits('close')">
      <view class="popup-container">
        <!-- SKU 信息 -->
        <view class="header-shopping-info">
          <view class="img-info">
            <img :src="state.selectedSku.picUrl || goodsInfo.picUrl">
          </view>
          <cn-money
              :money="fen2yuan(state.selectedSku.price || goodsInfo.price)"
              :size="46"
              color="#ee719b" />
        </view>

        <!-- 属性选择 -->
        <view class="shopping-sku-specification">
          <view class="sku-container" v-for="property in propertyList" :key="property.id">
            <view class="sku-title">
              {{ property.name }}
            </view>
            <view class="sku-spec">
              <template v-for="value in property.values" :key="value.id">
                <button
                    class="ss-reset-button sku-item"
                    :disabled="value.disabled === true"
                    :class="state.currentPropertyArray[property.id] === value.id ? 'hover' : ''"
                    @tap="onSelectSku(property.id, value.id)"
                >
                  {{ value.name }}
                </button>
              </template>
            </view>
          </view>
          <view class="buy-num-box">
            <view class="purchase-quantity-front">
              购买数量
            </view>
            <su-number-box :min="1" :max="state.selectedSku.stock" :step="1"
                           v-model="state.selectedSku.goods_num" @change="onNumberChange($event)" />
          </view>

        </view>

        <!-- 操作区 -->
        <view>
          <view class="confirm-add-to-shopping-cart" @tap="onAddCart">确定</view>
        </view>
      </view>
    </su-popup>
  </view>
</template>

<script setup>
import SuPopup from "@/sheep/ui/su-popup/su-popup"
import CnMoney from '@/sheep/components/cn-money/cn-money.vue'
import { ref, reactive, watch, computed } from 'vue'
import { convertProductPropertyList, fen2yuan } from '@/sheep/hooks/useGoods.js'
import SuNumberBox from "@/sheep/ui/su-number-box/su-number-box"
import sheep from '@/sheep'

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
})
const state = reactive({
  // 选中的 SKU
  selectedSku: {},
  // 当前选中的属性，实际是个 Map。key 是 property 编号，value 是 value 编号
  currentPropertyArray: [],
})

// SKU 列表
const skuList = computed(() => {
  let skuPrices = props.goodsInfo.skus
  console.log('props所传递的全部skus信息skuPrices', props.goodsInfo.skus)
  // 遍历skuPrices数组中的每个元素（每个sku）
  for (let price of skuPrices) {
    // 为每个sku的price对象添加一个新的属性value_id_array
    // 该属性的值是一个数组，包含当前sku的所有属性中的valueId值
    price.value_id_array = price.properties.map((item) => item.valueId)
    console.log('对属性值的添加price.value_id_array', price.value_id_array)
  }
  return skuPrices
})

watch(
    () => state.selectedSku,
    (newVal) => {
      console.log('改变', newVal)
      emits('change', newVal);
    }, {
      immediate: true, // 立即执行
      deep: true, // 深度监听
    },
)


const propertyList = convertProductPropertyList(props.goodsInfo.skus)
console.log('props.goodsInfo.skus', props.goodsInfo.skus)
console.log('propertyList', propertyList)
console.log('规格', propertyList)

/**
 * 点击选中规格
 *
 * @param propertyId 父类id
 * @param valueId 子类id
 */
const onSelectSku = (propertyId, valueId) => {
  // 清空已选择
  let isChecked = true // 选中 or 取消选中

  // 选中 Map<propertyId, valueId>
  state.currentPropertyArray[propertyId] = valueId

  // 当前所选 property 下，所有可以选择的 SKU 们
  let newSkuList = getCanUseSkuList()
  console.log('当前所选 property 下，所有可以选择的 SKU 们', newSkuList)
  newSkuList[0].goods_num = state.selectedSku.goods_num || 1
  state.selectedSku = newSkuList[0]
}

const getCanUseSkuList = () => {
  let newSkus = []
  //
  for (let sku of skuList.value) {
    if (sku.stock <= 0) {
      continue;
    }
    let isOk = true;
    state.currentPropertyArray.forEach((propertyId) => {
      // propertyId 不为空，并且，这个 条 sku 没有被选中，则排除
      if (propertyId.toString() !== '' && sku.value_id_array.indexOf(propertyId) < 0) {
        isOk = false
      }
    });
    if (isOk) {
      newSkus.push(sku)
    }
  }
  return newSkus
}

// 添加到购物车中
const onAddCart = () => {
  if (state.selectedSku.id <= 0 || state.selectedSku.id === undefined) {
    sheep.$helper.toast('请选择规格')
    return
  }
  if (state.selectedSku.stock <= 0) {
    sheep.$helper.toast('库存不足')
    return
  }
  // console.log('添加的sku信息',state.selectedSku)
  emits('addCart', state.selectedSku)
}

// 输入框改变数量
function onNumberChange(e) {
  console.log(e)
  if (e === 0) {
    return;
  }
  if (state.selectedSku.goods_num === e) {
    return
  }
  state.selectedSku.goods_num = e;
}
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
          height: 200rpx;
          /* 只显示一个图片， */
          cursor: pointer;
          /* 图片比例缩放 */
          /* 使用 cover 填充整个容器，可能裁剪部分图片 */
          object-fit: cover;
          background: #f6f7fb;
          border-radius: 20rpx;
        }
      }

    }
    .shopping-sku-specification {
      margin: 30rpx 16rpx;
      flex: 1;
      font-size: 24rpx;
      .sku-container{
        gap: 20rpx;
        .sku-title {
          font-weight: 600;
          margin: 20rpx 0;
        }
        .sku-spec {
          display: flex;
          gap: 20rpx;
          flex-wrap: wrap;
          .sku-item {
            font-size: 22rpx;
            background: #f6f6f6;
            padding: 6rpx 24rpx;
            border-radius: 30rpx;
          }
          .sku-item.hover {
            background: #fef4f3;
            border: 1rpx solid #b9301b;
            color: #b54f46;
          }
        }
      }
      .buy-num-box {
        margin: 30rpx 0;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .purchase-quantity-front {
          font-weight: 600;
        }
      }

    }
    .confirm-add-to-shopping-cart {
      width: 100%;
      background: linear-gradient(to right, #f33f4c, #f43940, #f9332a, #fb2e1b);
      height: 66rpx;
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
