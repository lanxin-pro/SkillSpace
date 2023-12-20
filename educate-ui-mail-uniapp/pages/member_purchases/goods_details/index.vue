<template>
  <view class="product-container">
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
    <view>
      <scroll-view :scroll-top="scrollTop" scroll-y='true' scroll-with-animation="true"
                   :style='"height:" + height + "px;"' @scroll="scroll">
        <view id="past0">
          <!-- 商品轮播图  -->
          <su-swiper
              isPreview
              dotStyle="tag"
              imageMode="widthFix"
              :list="[{'type': 'image','src': 'http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=My5qcGc=&version_id=null'},
                      {'type': 'image','src': 'http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=Mi5qcGc=&version_id=null'},
                      {'type': 'image','src': 'http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=MDAxLmpwZw==&version_id=null'},
                      {'type': 'image','src': 'http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=NC5qcGc=&version_id=null'},
                      {'type': 'image','src': 'http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=NS5qcGc=&version_id=null'},
                      {'type': 'image','src': 'http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=Ni5qcGc=&version_id=null'},
            ]"
              dotCur="bg-mask-40"
              :seizeHeight="750"
          />
        </view>
        <view class="amount-area">
          <view class="money-area-left">
            <view>
              <text class="font-weight-bold title">定金</text>
              <cn-money :size="46" color="#fffff" :money="16.35"  />
            </view>
            <view class="optimized-display">
              <view class="postcoupon">
                新人劵后 ￥275
                <text class="iconfont icon-xiangyou" />
              </view>
              <view class="original-price">
                原价￥285
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
              ANIPLEX+ 孤独摇滚！ 后女仆Ver.手办我的期待
            </view>
            <view class="collect-number">
              <view class="collect">
                <text class="iconfont icon-shoucang"></text>
              </view>
              <view class="desired-number">
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
            参数
          </view>

          <view>
            预售流程
          </view>

          <view>
            分类？
          </view>
          <DetailCell @top="test" />
        </view>

        <view id="past1">
          <view v-for="(item) of 30">评价页面</view>
        </view>
        <view id="past2">
          <view v-for="(item) of 40">详细页面</view>
        </view>
        <text class='iconfont icon-shouye-xianxing'></text>

        <!-- 详情 tabbar -->
        <DetailTabbar />
      </scroll-view>
<!--  <DetailTabbar /> 直接放到scroll-view的外面也是可以的    -->

    </view>
  </view>
</template>

<script>
import * as ProductSpuApi from '@/api/product/spu.js'
import SuSwiper from '@/components/ui/su-swiper/su-swiper.vue'
import DetailCell from './../components/detail-cell'
import DetailTabbar from './../components/detail-tabbar'

// APP.vue全局的作用域
const app = getApp()

export default {
  components: {
    SuSwiper, DetailCell, DetailTabbar
  },
  data() {
    return {

      // ========== 商品相关变量 ==========
      spu: {}, // 商品 SPU 详情
      skuMap: [], // 商品 SKU Map

      // ========== 顶部 nav + scroll 相关的变量 ==========
      returnShow: true, // 判断顶部 [返回] 是否出现
      homeTop: 20, // 头部的 top 位置
      clientHeight: "",  // 客户端 height 高度
      height: 0, // 窗口 height 高度
      scrollY: 0, // 滚动的 Y 轴
      scrollTop: 0,  // 滚动条的 top 位置
      lock: false,  // 是否锁定 scroll 下
      topArr: [], // 每个 nav 的 top 位置
      heightArr: [], // 每个 nav 的 height 高度
      navH: "", // 头部 nav 高度
      navbarRight: 0,  // 头部 nav 距离 right 距离
      opacity: 0, // 头部 nav 的透明度
      navList: ['商品','评价','详情'], // 头部 nav 列表
      navActive: 0, // 选中的 navList 下标

    }
  },
  onLoad(options) {
    // 设置返回、nav 高度、总高度等字段
    // getCurrentPages函数用于获取当前页面栈的实例，以数组形式按栈的顺序给出，数组中的元素为页面实例，第一个元素为首页，最后一个元素为当前页面。
    const pages = getCurrentPages()
    // TODO j-sentinel 测试方便，我就先注释掉了
    // this.returnShow = pages.length !== 1
    this.navH = app.globalData.navHeight
    uni.getSystemInfo({
      success: ( res ) => {
        this.height = res.windowHeight
      }
    })
    // 校验参数是否正确 !options.scene = true
    if (!options.scene && !options.id) {
      this.$util.Tips({
        title: '缺少参数无法查看商品！'
      }, {
        url: '/pages/member_purchase/member_purchase'
      })
      return
    }
    let that = this
    this.id = options.id

    // 请求后端，加载商品等相关信息
    this.getGoodsDetails()
  },
  methods: {
    test: function(){
      console.log('最外层的点击事件')
    },
    getGoodsDetails: function() {
      ProductSpuApi.getSpuDetail(this.id).then(res => {
        console.log('res的结果', res)
        let spu = res.data

        // 处理滚动条
        setTimeout(() => {
          this.infoScroll();
        }, 1000);

      }).catch(err => {
        return this.$util.Tips({
          title: err.toString()
        }, {
          tab: 3,
          url: 1
        });
      })
    },

    // ========== 顶部 nav 相关的方法 ==========
    /**
     * 处理器滚动条
     */
    infoScroll: function() {
      const topArr = []
      const heightArr = []
      for (let i = 0; i < this.navList.length; i++) {
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
        topArr.push(totalOffsetTop)
        // 元素的高度
        heightArr.push(elementHeight)
        // 每个 nav 的 top 位置
        this.$set(this, 'topArr', topArr)
        // 每个 nav 的 height 高度
        this.$set(this, 'heightArr', heightArr)
      }
    },
    /**
     * 后退
     */
    returns: function() {
      uni.navigateBack()
    },
    /**
     * 滚动
     *
     * @param e 滚动事件
     */
    scroll: function(e) {
      const	scrollY = e.detail.scrollTop;
      this.scrollTop = scrollY
      let opacity = scrollY / 400;
      opacity = opacity > 0.85 ? 0.85 : opacity;
      this.$set(this, 'opacity', opacity);
      this.$set(this, 'scrollY', scrollY);
      if (this.lock) {
        this.$set(this, 'lock', false)
        return;
      }
      // 设置选中的 nav
      for (let i = 0; i < this.topArr.length; i++) {
        if (scrollY < this.topArr[i] - (app.globalData.navHeight / 2) + this.heightArr[i]) {
          this.$set(this, 'navActive', i)
          break
        }
      }
    },
    /**
     * 点击指定 nav bar
     *
     * @param index 新的 navList 位置
     */
    tap: function(index) {
      this.$set(this, 'navActive', index)
      this.$set(this, 'lock', true)
      this.$set(this, 'scrollTop', index > 0 ? this.topArr[index] - (app.globalData.navHeight / 2)
          : this.topArr[index])
    },

  }
}
</script>

<style scoped lang="scss">
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
    padding: 15rpx 25rpx 25rpx;
    background: #ffffff;
    border-radius: 15rpx;
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
