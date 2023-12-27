<!-- 此组件在使用的时候希望能给出seizeHeight属性的值 -->
<template>
  <view>2</view>
</template>

<script setup>
import { reactive, computed } from 'vue'
// 数据
const state = reactive({
  imgHeight: 0,
  cur: 0,
  moveX: 0,
  videoPlaySataus: false,
  heightList: [],
});
// 自动计算高度
const customStyle = computed(() => {
  let height;

  // 固定高度情况
  if (props.height !== 0) {
    height = props.height;
  }

  // 自动高度情况
  if (props.height === 0) {
    // 图片预加载占位高度
    if (state.imgHeight !== 0) {
      height = state.imgHeight;
    } else if (props.seizeHeight !== 0) {
      height = props.seizeHeight;
    }
  }

  return {
    height: height + 'rpx',
  };
});
// 接收参数

const props = defineProps({
  circular: {
    type: Boolean,
    default: true,
  },
  autoplay: {
    type: Boolean,
    default: false,
  },
  interval: {
    type: Number,
    default: 3000,
  },
  duration: {
    type: Number,
    default: 500,
  },
  mode: {
    type: String,
    default: 'default',
  },
  imageMode: {
    type: String,
    default: 'scaleToFill',
  },
  list: {
    type: Array,
    default() {
      return [];
    },
  },
  dotStyle: {
    type: String,
    default: 'long', //default long tag
  },
  dotCur: {
    type: String,
    default: 'ss-bg-opactity-block',
  },
  bg: {
    type: String,
    default: 'bg-none',
  },
  height: {
    type: Number,
    default: 0,
  },
  imgHeight: {
    type: Number,
    default: 0,
  },
  imgTopRadius: {
    type: Number,
    default: 0,
  },
  imgBottomRadius: {
    type: Number,
    default: 0,
  },
  isPreview: {
    type: Boolean,
    default: false,
  },
  seizeHeight: {
    type: Number,
    default: 200,
  },
})

</script>

<style lang="scss" scoped>
.ui-swiper {
  position: relative;

  .ui-swiper-main {
    width: 100%;
    height: 100%;
  }

  .ui-swiper-main .swiper-image {
    width: 100%;
    height: 100%;
  }

  .ui-swiper-dot {
    position: absolute;
    width: 100%;
    bottom: 20rpx;
    height: 30rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #eeeeee;
    .bg-mask-40:before {
      content: "";
      border-radius: inherit;
      width: 100%;
      height: 100%;
      position: absolute !important;
      top: 0;
      right: 0;
      bottom: 0;
      left: 0;
      margin: auto;
      background-color: rgba(0,0,0,.4);
      z-index: 0;
    }
    .ui-tag {
      position: absolute;
      top: 0;
      right: 0;
      border-radius: 6rpx 0 6rpx 0;
    }
    .radius-lg {
      border-radius: 40rpx;
    }

    &.default .line-box {
      display: inline-flex;
      border-radius: 50rpx;
      width: 6px;
      height: 6px;
      border: 2px solid transparent;
      margin: 0 10rpx;
      opacity: 0.3;
      position: relative;
      justify-content: center;
      align-items: center;

      &.cur {
        width: 8px;
        height: 8px;
        opacity: 1;
        border: 0px solid transparent;
      }

      &.cur::after {
        content: '';
        border-radius: 50rpx;
        width: 4px;
        height: 4px;
        background-color: #fff;
      }
    }

    &.long .line-box {
      display: inline-block;
      border-radius: 100rpx;
      width: 6px;
      height: 6px;
      margin: 0 10rpx;
      opacity: 0.3;
      position: relative;

      &.cur {
        width: 24rpx;
        opacity: 1;
      }

      &.cur::after {
      }
    }

    &.line {
      bottom: 20rpx;

      .line-box {
        display: inline-block;
        width: 30px;
        height: 3px;
        opacity: 0.3;
        position: relative;

        &.cur {
          opacity: 1;
        }
      }
    }

    &.tag {
      justify-content: flex-end;
      position: absolute;
      bottom: 20rpx;
      right: 20rpx;
    }
  }

  &.card {
    .swiper-item {
      width: 610rpx !important;
      left: 70rpx;
      box-sizing: border-box;
      padding: 20rpx 0rpx 60rpx;
      overflow: initial;
    }

    .swiper-item .ui-swiper-main {
      width: 100%;
      display: block;
      height: 100%;
      transform: scale(0.9);
      transition: all 0.2s ease-in 0s;
      position: relative;
      background-size: cover;

      .swiper-image {
        height: 100%;
      }
    }

    .swiper-item .ui-swiper-main::before {
      content: '';
      display: block;
      background: inherit;
      filter: blur(5px);
      position: absolute;
      width: 100%;
      height: 100%;
      top: 10rpx;
      left: 10rpx;
      z-index: -1;
      opacity: 0.3;
      transform-origin: 0 0;
      transform: scale(1, 1);
    }

    .swiper-item.cur .ui-swiper-main {
      transform: scale(1);
      transition: all 0.2s ease-in 0s;
    }

    .ui-swiper-dot.tag {
      position: absolute;
      bottom: 85rpx;
      right: 75rpx;
    }
  }

  &.hotelCard {
    .swiper-item {
      width: 650rpx !important;
      left: 30rpx;
      box-sizing: border-box;
      padding: 0rpx 0rpx 50rpx;
      overflow: initial;
    }

    .swiper-item .ui-swiper-main {
      width: 100%;
      display: block;
      height: 100%;
      transform: scale(0.9);
      opacity: 0.8;
      transition: all 0.2s ease-in 0s;
      position: relative;
      background-size: cover;

      .swiper-image {
        width: 100%;
        height: 400rpx;
      }
    }

    .swiper-item .ui-swiper-main::before {
      content: '';
      display: block;
      background: inherit;
      filter: blur(5px);
      position: absolute;
      width: 100%;
      height: 100%;
      top: 10rpx;
      left: 10rpx;
      z-index: -1;
      opacity: 0.3;
      transform-origin: 0 0;
      transform: scale(1, 1);
    }

    .swiper-item.cur .ui-swiper-main {
      transform: scale(1);
      transition: all 0.2s ease-in 0s;
      opacity: 1;
    }

  }

  &.hotelDetail {
    .swiper-item {
      width: 690rpx !important;
      left: 30rpx;
      box-sizing: border-box;
      padding: 20rpx 0rpx;
      overflow: initial;
    }

    .swiper-item .ui-swiper-main {
      width: 100%;
      display: block;
      height: 100%;
      transform: scale(0.96);
      transition: all 0.2s ease-in 0s;
      position: relative;
      background-size: cover;

      .swiper-image {
        height: 100%;
      }
    }

    .swiper-item.cur .ui-swiper-main {
      transform: scale(0.96);
      transition: all 0.2s ease-in 0s;
    }
  }
}
</style>
