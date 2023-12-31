<template>
  <view class="product-container">

    <!-- 骨架屏 -->
    <detail-skeleton v-if="state.skeletonLoading" />
    <!--  骨架屏会触发一些bug，所以这里就在加载的时候关闭  -->
    <template v-if="!state.skeletonLoading">
      <!-- 顶部的 nav tab -->
      <view class='navbar' :style="{ height: navH + 'rpx', opacity: opacity }">
        <view class='navbarH' :style='"height:" + navH + "rpx;"'>
          <view class='navbarCon acea-row row-center-wrapper' :style="{ paddingRight: navbarRight + 'px' }">
            <view class="header acea-row row-center-wrapper">
              <view class="item" :class="navActive === index ? 'on animated fadeIn' : ''"
                    v-for="(item, index) in navList" :key='index' @tap="tap(index)">
                {{ item }}
              </view>
            </view>
          </view>
        </view>
      </view>
      <!-- 返回键 -->
      <!--     opacity这个属性真的有0.001     -->
      <view id="home" class="home-nav-left acea-row row-center-wrapper iconfont icon-xiangzuo"
            :class="opacity > 0.001 ? 'on' : ''"
            :style="{ top: homeTop + 'rpx', opacity: opacity > 0.001 ? opacity : ''}"
            v-if="returnShow"
            @tap="returns"
      />
      <!-- 分享键 依旧是同理 -->
      <view id="share" class="share-nav-right acea-row row-center-wrapper iconfont icon-fenxiang"
            :class="opacity > 0.001 ? 'on' : ''"
            :style="{ top: homeTop + 'rpx', opacity: opacity > 0.001 ? opacity : ''}"
            v-if="returnShow"
            @tap="returns"
      />
      <scroll-view :scroll-top="scrollTop" scroll-y='true' scroll-with-animation="true"
                   :style='"height:" + height + "px;"' @scroll="scroll">

        <view id="past0">
          <!-- 商品轮播图  -->
          <su-swiper
              isPreview
              dotStyle="tag"
              imageMode="widthFix"
              :list="formatGoodsSwiper(state.goodsInfo.sliderPicUrls)"
              dotCur="bg-mask-40"
              :seizeHeight="750"
          />
        </view>
        <view class="amount-area">
          <view class="money-area-left">
            <view>
              <text class="font-weight-bold title">定金</text>
              <cn-money :size="46" color="#fffff" :money="fen2yuan(state.goodsInfo.price)"  />
            </view>
            <view class="optimized-display">
              <view class="postcoupon">
                新人劵后 ￥{{ fen2yuan(state.goodsInfo.price) }}
                <text class="iconfont icon-xiangyou" />
              </view>
              <view class="original-price">
                原价￥{{ fen2yuan(state.goodsInfo.marketPrice) }}
              </view>
            </view>
          </view>
          <view class="money-area-right">
            <view class="presell" v-if="true">
              <view>
                定金预售
              </view>
              <view>
                距离结束53天
              </view>
            </view>
            <view class="sell-out" v-else>
              <view>售完为止</view>
            </view>
          </view>
        </view>
        <view class="property">
          <view class="coupon-area">
            <view class="flex">
              <view class="reduce">
                满139减10元
              </view>
              <view class="reduce">
                满99减8元
              </view>
              <view class="reduce">
                立减5元
              </view>
            </view>
            <view class="go-get">
              领劵
              <text class="iconfont icon-xiangyou" />
            </view>
          </view>
          <view class="newcomer-discount">
            <view class="new-user-coupon-pack-price-container">
              <view class="new-user-coupon-pack">
                <view data-v-a6a54e38="" class="price-symbol">¥</view>
                <view data-v-a6a54e38="" class="price-2">50</view>
              </view>
              <view class="new-user-coupon-pack-content">
                新人专享礼包，及领取收单包邮
              </view>
              <view class="new-user-coupon-pack-receive-btn">立即领取</view>
            </view>


          </view>
          <view class="commodity-header-area">
            <view class="commodity-title">
              <!--       TODO j-sentinel 这里的文字长度应该进行优化       -->
              {{ state.goodsInfo.name }}+{{ state.goodsInfo.introduction }}
            </view>
            <view class="collect-number">
              <view class="collect">
                <!--            <view class="collect" @click="setCollect">-->
                <!--        收藏        -->
                <text v-if="state.goodsInfo.favorite" style="color: #fb759b" class="iconfont icon-shoucang1"></text>
                <text v-else class="iconfont icon-shoucang"></text>
              </view>
              <view class="desired-number">
                <!--       TODO j-sentinel 后期可以优化这里，思考一下怎么进行架构设计         -->
                3649人想要
              </view>
            </view>

          </view>
          <view class="ranking-list">
            <view class="ranking-left">
              <image
                  class="swiper-image"
                  src="@/static/images/member-purchase/project/ranking-list.png"
              />
              <text class="character">Myethos榜 No.1</text>
            </view>
            <view class="ranking-right">
              <text class="iconfont icon-xiangyou"></text>
            </view>
          </view>
        </view>
        <view class="empty">

        </view>

        <view class="commodity-size">
          <view>
            <DetailSpecification />
          </view>

          <view style="margin-top: 20rpx">
            <DetailProcedure />
          </view>

          <view class="commodity-category">
            <view class="tagC-ip">
              <img class="tagC-ip-icon" src="http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=ZmI4ZGZmODJiMjc1YmQyOWQ2MWNmZTVmNTJhYTQzODYucG5n&version_id=null">
              <div class="tagC-ip-con">
                五等分的新娘
              </div>
              <div class="tagC-ip-right iconfont icon-xiangyou"></div>
            </view>
            <view class="tagC-ip">
              <img class="tagC-ip-icon" src="http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=ZWU2NTM1MzExZmYxODdkYTE4YjAyNWJkYWExNGY1ZjAuanBn&version_id=null">
              <div class="tagC-ip-con">
                <view>
                  FuRyu
                </view>
                <view style="color: #7d7d7d;font-size: 18rpx;">
                  综合评分
                  <uni-rate
                      class="display-inline-block"
                      :readonly="true"
                      :size="10"
                      :value="4.5"
                      color="#dfe6e4"
                      active-color="#fa6c9c"
                  />
                </view>

              </div>
              <div class="tagC-ip-right iconfont icon-xiangyou"></div>
            </view>

          </view>

          <DetailCellSafeguard />

          <!-- 规格与数量弹框 -->
          <l-select-sku :goodsInfo="state.goodsInfo" :show="state.showSelectSku" @addCart="onAddCart"
                        @close="state.showSelectSku = false" />
        </view>

        <!--   评论   -->
        <view class="detail-comment-card" id="past1">
          <DetailCommentCard />
        </view>

        <!--   商品详情   -->
        <view class="detail-content-card" id="past2">
          <DetailContentCard />
        </view>


        <!--   TODO j-sentinel 这里的布局有些问题     -->
        <view class="pb150" />




      </scroll-view>

      <!--  <DetailTabbar /> 直接放到scroll-view的外面也是可以的    -->
      <!-- 底部导航，详情 tabbar -->
      <DetailTabbar
          :cartCount="cartCount"
          @addCartPopup="state.showSelectSku = true"
          :price="fen2yuan(state.goodsInfo.price)"
      />
    </template>

  </view>
</template>

<script setup>
import SuSwiper from '@/sheep/ui/su-swiper/su-swiper.vue'
import CnMoney from '@/sheep/components/cn-money/cn-money.vue'
import DetailTabbar from './components/detail-tabbar.vue'
import DetailSpecification from './components/detail-specification.vue'
import DetailCellSafeguard from './components/detail-cell-safeguard.vue'
import DetailProcedure from './components/detail-procedure.vue'
import DetailCommentCard from './components/detail-comment-card.vue'
import DetailContentCard from './components/detail-content-card.vue'
import DetailSkeleton from './components/detail-skeleton.vue'
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
import { isEmpty } from 'lodash'
import * as ProductSpuApi from '@/sheep/api/product/spu.js'
import * as FavoriteApi from '@/sheep/api/product/favorite.js'
import LSelectSku from "@/sheep/components/l-select-sku/l-select-sku"
import { formatGoodsSwiper, fen2yuan } from '@/sheep/hooks/useGoods.js'
import sheep from '@/sheep'

const cart = sheep.$store('cart')


const state = reactive({
  goodsId: 0,
  // SPU 加载中
  skeletonLoading: true,
  // SPU 信息
  goodsInfo: {},
  // 是否展示 SKU 选择弹窗
  /* 为了测试方便，这里就先给true */
  showSelectSku: false,
  // 选中的 SKU
  selectedSku: {},
  // 是否展示 Coupon 优惠劵的弹窗
  showModel: false,
  // 可领取的 Coupon 优惠劵的列表
  couponInfo: [],
  // 【满减送/限时折扣】是否展示 Activity 营销活动的弹窗
  showActivityModel: false,
  // 【满减送/限时折扣】可参与的 Activity 营销活动的列表
  activityInfo: [],
  // 【秒杀/拼团/砍价】可参与的 Activity 营销活动的列表
  activityList: [],
})

// ========== 顶部 nav + scroll 相关的变量 ==========
const returnShow = ref(true) // 判断顶部 [返回] 是否出现
const homeTop = ref(20) // 头部的 top 位置
const clientHeight = ref("")  // 客户端 height 高度
const height = ref(0) // 窗口 height 高度
const scrollY = ref(0) // 滚动的 Y 轴
const scrollTop = ref(0)  // 滚动条的 top 位置
const lock = ref(false)  // 是否锁定 scroll 下
/* TODO j-sentinel BUG：为什么这里使用ref就无法赋值？？？ */
const topArr = ref([]) // 每个 nav 的 top 位置
const heightArr = ref([]) // 每个 nav 的 height 高度
const navH = ref("") // 头部 nav 高度
const navbarRight = ref(0)  // 头部 nav 距离 right 距离
const opacity = ref(0) // 头部 nav 的透明度
const navList = ref(['商品','评价','详情']) // 头部 nav 列表
const navActive = ref(0) // 选中的 navList 下标

const globalData = reactive({
  navHeight: 0
})

// 购物车数量
const cartCount = computed(() => {
  return cart.list.length
})

onLoad( async (options) => {
  // 非法参数
  if (!options.id) {
    state.goodsInfo = null
    alert('非法参数')
    return
  }
  // 处理页面参数
  initPageParameter()

  state.goodsId = options.id
  const response = await ProductSpuApi.getSpuDetail(state.goodsId)
  // 未找到商品
  if(response.code != 0 || !response.data) {
    state.goodsInfo = null
    return
  }
  setTimeout(() => {
    // 加载商品
    state.skeletonLoading = false
  }, 500)
  state.goodsInfo = response.data
  // 加载是否收藏
  FavoriteApi.isFavoriteExists(state.goodsId, 'goods').then((res) => {
    if (res.code !== 0) {
      return
    }
    state.goodsInfo.favorite = res.data
  })




})

/* 添加购物车 */
const onAddCart = (e) => {
  if (!e.id) {
    sheep.$helper.toast('请选择商品规格')
    return;
  }
  sheep.$store('cart').add(e)
}


// ========== 顶部 nav 相关的方法 ==========
const initPageParameter = () => {
  // 计算页面tab的高度
  uni.getSystemInfo({
    success: function (res) {
      globalData.navHeight = res.statusBarHeight * (750 / res.windowWidth) + 91;
    }
  })

  navH.value = globalData.navHeight

  uni.getSystemInfo({
    success: ( res ) => {
      height.value = res.windowHeight
    }
  })
  // 处理滚动条
  setTimeout(() => {
    infoScroll()
  }, 1000);
}
/**
 * 处理器滚动条
 */
const infoScroll = function() {
  const topArray = []
  const heightArray = []
  for (let i = 0; i < navList.value.length; i++) {
    // 获取元素
    let element = document.getElementById("past" + i);
    // 获取元素的高度
    let elementHeight = element.offsetHeight
    // 获取元素相对于文档顶部的总体偏移量
    let totalOffsetTop = 0
    while (element) {
      totalOffsetTop += element.offsetTop
      element = element.offsetParent
    }
    // 元素相对于文档顶部的总体偏移量
    topArray.push(totalOffsetTop)
    // 元素的高度
    heightArray.push(elementHeight)
    // 每个 nav 的 top 位置
    topArr.value = topArray
    // 每个 nav 的 height 高度
    heightArr.value = heightArray
  }
}
/**
 * 后退
 */
const returns = () => {
  uni.navigateBack({
    delta: 1
  })
}
/**
 * 滚动
 *
 * @param e 滚动事件
 */
const scroll = function(e) {
  const	scrollYs = e.detail.scrollTop
  scrollTop.value = scrollYs
  let letOpacity = scrollYs / 400
  letOpacity = letOpacity > 0.85 ? 0.85 : letOpacity
  opacity.value = letOpacity
  scrollY.value = scrollYs
  if (lock.value) {
    lock.value = false
    return;
  }
  // 设置选中的 nav
  for (let i = 0; i < topArr.value.length; i++) {
    if (scrollY.value < topArr.value[i] - (globalData.navHeight / 2) + heightArr.value[i]) {
      navActive.value = i
      break
    }
  }
}
/**
 * 点击指定 nav bar
 *
 * @param index 新的 navList 位置
 */
const tap = function(index) {
  navActive.value = index
  lock.value = true
  scrollTop.value = index > 0 ? topArr.value[index] - (globalData.navHeight / 2)
      : topArr.value[index]
}
</script>

<style scoped lang="scss">
/* 下面我们会解释这些 class 是做什么的 */
.v-enter-active,
.v-leave-active {
  transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
.product-container {
  height: 100%;
  /*返回主页按钮*/
  .home-nav-left {
    /* #ifdef H5 */
    top: 15rpx !important;
    /* #endif */
    color: rgb(120, 119, 115);
    position: fixed;
    font-size: 33rpx;
    width: 56rpx;
    height: 56rpx;
    line-height: 56rpx;
    /* 防止uni-popup冲突 */
    z-index: 1;
    left: 33rpx;
    background: rgba(212, 210, 210, 0.8);
    border-radius: 50%;
    .icon-xiangzuo {
      width: auto;
      font-size: 28rpx;
    }
    .line {
      width: 1rpx;
      height: 24rpx;
      background: rgba(255, 255, 255, 0.25);
    }
    &.on{
      background: rgb(251, 251, 251);
      color: #333;
      z-index: 99;
    }
  }
  .share-nav-right {
    /* #ifdef H5 */
    top: 15rpx !important;
    /* #endif */
    color: rgb(120, 119, 115);
    position: fixed;
    font-size: 33rpx;
    width: 56rpx;
    height: 56rpx;
    line-height: 56rpx;
    /* 防止uni-popup冲突 */
    z-index: 1;
    right: 33rpx;
    background: rgba(212, 210, 210, 0.8);
    border-radius: 50%;
    .icon-fenxiang {
      width: auto;
      font-size: 28rpx;
    }
    .line {
      width: 1rpx;
      height: 24rpx;
      background: rgba(255, 255, 255, 0.25);
    }
    &.on{
      background: rgb(251, 251, 251);
      color: #333;
      z-index: 99;
    }
  }
  .amount-area {
    padding: 10rpx 25rpx;
    background: #fafcfa;
    border-radius: 0 0 10rpx 10rpx;
    color: #f57097;
    display: flex;
    justify-content: space-between;
    .money-area-left {
      display: flex;
      flex-direction: column;
      .title {
        font-size: 28rpx;
        margin-right: 8rpx;
      }
      .optimized-display {
        display: flex;
        align-items: center;
        .postcoupon {
          border-radius: 20rpx;
          background: #feedf3;
          padding: 7rpx 5rpx 7rpx 10rpx;
          font-size: 22rpx;
          margin: 5rpx 0 0 -7rpx;
          text {
            font-size: 20rpx;
          }
        }
        .original-price {
          font-size: 22rpx;
          text-decoration: line-through;
          margin-left: 13rpx;
          color: black;
        }
      }
    }
    .money-area-right {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: end;
      background-image: url("@/static/images/member-purchase/project/money-background.png");
      background-size: cover;
      background-position: center center; /* 设置背景图片位置，居中显示 */
      background-repeat: no-repeat; /* 防止背景图片重复显示 */
      width: 300rpx;
      .presell {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: end;
        font-size: 26rpx;
      }
      .sell-out {

      }
    }
  }
  .property {
    padding: 25rpx;
    background: #ffffff;
    border-radius: 0 0 20rpx 20rpx;
    .coupon-area {
      display: flex;
      justify-content: space-between;
      .reduce {
        border-radius: 8rpx;
        padding: 5rpx 12rpx;
        font-size: 22rpx;
        font-weight: 600;
        background: #feeff4;
        color: #ed769f;
        margin-left: 10rpx;
      }
      .reduce:first-child {
        margin-left: 0;
      }
      .go-get {
        color: #ffffff;
        background: #ff689b;
        font-size: 22rpx;
        border-radius: 19rpx 0 0 19rpx;
        margin-right: -26rpx;
        padding: 5rpx 30rpx 5rpx 16rpx;
        text {
          font-size: 22rpx;
          margin-left: 5rpx;
        }
      }

    }
    .newcomer-discount {
      margin-top: 17rpx;
      .new-user-coupon-pack-price-container {
        background-image: url(http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=VUw0TFFPUjR2Ri5wbmc=&version_id=null);
        background-size: 100% 100%;
        background-repeat: no-repeat;
        font-size: 28rpx;
        height: 84rpx;
        padding: 30rpx;
        margin-bottom: 24rpx;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .new-user-coupon-pack {
          color: #ff007a;
          display: flex;
          align-items: center;
          .price-symbol {
            font-family: PingFang SC;
            font-size: 16px;
            font-weight: 500;
            line-height: 28px;
            margin-right: 2px;
            color: #ff007a;
          }
          .price-2 {
            font-family: PingFang SC;
            font-size: 38rpx;
            font-weight: 600;
            line-height: 36rpx;
            letter-spacing: 0;
            text-align: center;
          }
        }
        .new-user-coupon-pack-content {
          font-family: PingFang SC;
          font-size: 12px;
          font-weight: 400;
          line-height: 17px;
          letter-spacing: 0;
          text-align: left;
          color: #18191c;
        }
        .new-user-coupon-pack-receive-btn {
          font-family: PingFang SC;
          font-size: 24rpx;
          font-weight: 500;
          line-height: 36rpx;
          letter-spacing: 0;
          text-align: center;
          color: #eeeeee;
        }
      }
    }
    .commodity-header-area {
      margin-top: 17rpx;
      display: flex;
      gap: 75rpx;
      .commodity-title {
        font-size: 30rpx;
        font-weight: 600;
        line-height: 45rpx;
      }
      .collect-number {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        margin-right: -6rpx;
        .collect text {
          font-size: 36rpx;
          font-weight: 600;
        }
        .desired-number {
          font-size: 18rpx;
          white-space: nowrap;
          margin-top: 8rpx;
        }
      }
    }
    .ranking-list {
      margin-top: 20rpx;
      padding-top: 20rpx;
      border-top: 1px solid rgb(231, 231, 231);
      display: flex;
      justify-content: space-between;
      align-items: center;
      .ranking-left {
        display: flex;
        gap: 17rpx;
        align-items: center;
        image {
          width: 140rpx;
          height: 38rpx;
        }
        .character {
          font-size: 24rpx;
        }
      }
      .ranking-right {
        text {
          font-size: 24rpx;
          color: rgb(205, 205, 205);
          font-weight: 600;
        }

      }

    }
  }
  .empty {
    margin: 20rpx 0 20rpx;
    padding: 15rpx 25rpx 25rpx;
    background: #ffffff;
    border-radius: 15rpx;
  }
  .commodity-size {
    padding: 30rpx 25rpx 20rpx;
    background: #ffffff;
    border-radius: 25rpx;
    .commodity-category {
      padding-bottom: 10rpx;
      margin: 20rpx 0;
      border-bottom: 1px solid #e7e7e7;
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
      .tagC-ip {
        display: flex;
        align-items: center;
        width: 48.5%;
        height: 80rpx;
        border: 1rpx solid #e7e7e7;
        border-radius: 12rpx;
        margin-bottom: 10rpx;
        padding: 0 4px;
        .tagC-ip-icon {
          width: 26px;
          height: 26px;
        }
        .tagC-ip-con {
          flex: 1;
          margin-left: 16rpx;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          padding: 8rpx 0;
          font-size: 22rpx;
        }
        .tagC-ip-right {
          font-size: 16rpx;
          padding: 5rpx;
          background: #d9d9d9;
          border-radius: 40rpx;
          color: #ffffff;
        }

      }
    }
  }
  .detail-comment-card {
    margin-top: 20rpx;
  }
  .detail-content-card {
    margin-top: 20rpx;
  }

}
.navbar {
  position: fixed;
  background-color: #fff;
  top: 0;
  left: 0;
  z-index: 99;
  width: 100%;
}
.navbar .navbarH {
  position: relative;
}
.navbar .navbarH .navbarCon {
  position: absolute;
  bottom: 0;
  height: 100rpx;
  width: 100%;
}
.navbar .header {
  height: 96rpx;
  font-size: 30rpx;
  color: #050505;
  background-color: #fff;
  /* #ifdef MP */
  padding-right: 95rpx;
  /* #endif */
}
.navbar .header .item {
  position: relative;
  margin: 0 25rpx;
}
/* item 本体文字 */
.navbar .header .item.on {
  font-weight: 600;
  font-size: 30rpx;
}
/* item:before下划线 */
.navbar .header .item.on:before {
  position: absolute;
  width: 50rpx;
  height: 5rpx;
  background-repeat: no-repeat;
  content: "";
  background-image: linear-gradient(to right, rgb(251, 114, 153) 0%, #ff6533 100%);
  bottom: -6rpx;
  left: 50%;
  margin-left: -26rpx;
}
</style>
