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
        <view v-for="(item) of 100">详细页面</view>
        <text  class='iconfont icon-shouye-xianxing'></text>
      </scroll-view>
    </view>
  </view>
</template>

<script>
// APP.vue全局的作用域
const app = getApp()

export default {
  data() {
    return {
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
    console.log(pages)
    this.navH = app.globalData.navHeight
    uni.getSystemInfo({
      success: ( res ) => {
        this.height = res.windowHeight
      }
    })
  },
  methods: {

    // ========== 顶部 nav 相关的方法 ==========
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
      let opacity = scrollY / 700;
      opacity = opacity > 0.8 ? 0.8 : opacity;
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
      this.$set(this, 'navActive', index);
      this.$set(this, 'lock', true);
      this.$set(this, 'scrollTop', index > 0 ? this.topArr[index] - (app.globalData.navHeight / 2)
          : this.topArr[index]);
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
    z-index: 999;
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
    z-index: 999;
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
