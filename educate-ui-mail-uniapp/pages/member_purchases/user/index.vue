<template>
	<view>
    <view class="box-nav-bar">
      <uni-nav-bar
          background-color="#ffffff"
          left-icon="left"
          right-icon="cart"
          title="会员购中心"
          size="20"
          :border="false"
          @clickLeft="clickLeft"
          @clickRight="clickRight"
      >
        <template slot="left">
          <text class="iconfont icon-xiangzuo"></text>
        </template>
        <template slot="right">
          <img class="right-img" src="../../../static/images/3-002.png" />
        </template>
      </uni-nav-bar>
    </view>
    <view class="user-info">

      <view class="left-info">
        <!-- 头像 -->
        <image class="user-avatar" src="../../../static/images/user-info/noface.jpg" />
        <view class="user-name">落寞帝妃夜呻吟</view>
      </view>
      <view class="right-promotion">
        <img src="../../../static/images/member-purchase/user/vane438b1466b3d94bb68dbba5e8609109cd.png" />
      </view>

    </view>
    <view class="order-form-category">
      <view class="title-header">
        <view class="left-title">
          我的订单
        </view>
        <view class="right-title">
          查看全部订单
          <view class="display-inline">
            <text class="iconfont icon-xiangyou"></text>
          </view>
        </view>
      </view>
      <view class="category">
        <view class="item">
          <img src="../../../static/images/member-purchase/user/category/001.png" />
          <view class="order-title">
            待付款
          </view>
        </view>
        <view class="item">
          <img src="../../../static/images/member-purchase/user/category/002.png" />
          <view class="order-title">
            待开补款
          </view>
        </view>
        <view class="item">
          <img src="../../../static/images/member-purchase/user/category/003.png" />
          <view class="order-title">
            待收货
          </view>
        </view>
        <view class="item">
          <img src="../../../static/images/member-purchase/user/category/004.png" />
          <view class="order-title">
            待评价
          </view>
        </view>
        <view class="item">
          <img src="../../../static/images/member-purchase/user/category/005.png" />
          <view class="order-title">
            退换/售后
          </view>
        </view>

      </view>
    </view>
    <view class="my-record">
      <view class="left-record">
        <view class="flex-re">
          <view>
            <img src="../../../static/images/member-purchase/user/shoucang.png" />
          </view>
          <view class="collect">
            我的收藏
          </view>
          <view class="num">
            （3）
          </view>
        </view>

        <view class="collect-img">
          <view>
            <img src="../../../static/images/member-purchase/user/history/1d578afb7196db7a8a2888a087d1a476.png" />
          </view>
          <view>
            <img src="../../../static/images/member-purchase/user/history/1d578afb7196db7a8a2888a087d1a476.png" />
          </view>
        </view>
      </view>
      <view class="vertical-line" />
      <view class="right-record">
        <view class="flex-re">
          <view>
            <img src="../../../static/images/member-purchase/user/glkZ9BC21601378860827.png" />
          </view>
          <view class="record">
            浏览记录
          </view>
        </view>
        <view class="collect-img">
          <view>
            <img src="../../../static/images/member-purchase/user/history/1d578afb7196db7a8a2888a087d1a476.png" />
          </view>
          <view>
            <img src="../../../static/images/member-purchase/user/history/1d578afb7196db7a8a2888a087d1a476.png" />
          </view>
        </view>
      </view>
    </view>
    <view class="information">
      <view class="discounts">
        <view class="number">
          <view>
            <cn-money :size="44" color="#fffff" :money="0" :showUnit="false" :showZeroDecimal="false" />
          </view>
          <view class="number-title">
            张
          </view>
        </view>
        <view class="title">
          优惠券
        </view>
      </view>
      <view class="discounts">
        <view class="number">
          <view>
           <cn-money :size="44" color="#fffff" :money="0" thousandth />
          </view>
        </view>
        <view class="title">
          红包
        </view>
      </view>
      <view class="discounts">
        <view class="number">
          <view>
           <cn-money :size="44" color="#fffff" :money="0" :showUnit="false" :showZeroDecimal="false" />
          </view>
          <view class="number-title">
            份
          </view>
        </view>
        <view class="title">
          我的奖品
        </view>
      </view>
    </view>
	</view>
</template>

<script>
export default {
  methods: {
    clickLeft(){
      uni.navigateBack({
        delta: 1  // 返回的页面数，如果是1，则返回上一级页面
      });

    },
    clickRight(){
      uni.switchTab({
        url: "/pages/member_purchase/member_purchase"
      });

    }
  }
}
</script>

<style lang="scss" scoped>
.box-nav-bar {
  .right-img {
    width: 40rpx;
  }
}

.user-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 40rpx 30rpx 30rpx;
  .left-info {
    display: flex;
    align-items: center;
    .user-name {
      margin-left: 25rpx;
      white-space: nowrap;  /* 确保文本在一行内显示 */
      overflow: hidden;     /* 隐藏超出元素宽度的文本 */
      text-overflow: ellipsis; /* 使用省略号表示被截断的文本 */
      width: 190rpx; /* 你需要设置一个宽度限制 */
      font-size: 32rpx;
    }
    .user-avatar {
      cursor: pointer;
      width: 55px;
      height: 55px;
      border-radius: 50%;
    }
  }
  .middle-grade {
    font-size: 22rpx;
  }
  .right-promotion {
    img {
      width: 260rpx;
    }
  }
}
.order-form-category {
  background: #ffffff;
  border-radius: 30rpx;
  padding: 30rpx 30rpx 15rpx;
  .title-header {
    display: flex;
    justify-content: space-between;
    .left-title {
      font-weight: 600;
      font-size: 24rpx;
      color: #030303;
    }
    .right-title {
      line-height: 30rpx;
      font-size: 18rpx;
      color: #545454;
      .iconfont {
        font-size: 20rpx;
        font-weight: 600;
        color: #b1b0b6;
      }
    }
  }
  .category {
    display: flex;
    padding: 30rpx 20rpx;
    justify-content: space-between;
    .item {
      display: flex;
      flex-direction: column;
      align-items: center;
      img {
        width: 55rpx;
      }
      .order-title {
        margin-top: 8rpx;
        font-size: 20rpx;
        color: #626363;
      }
    }
  }
}
.my-record {
  margin-top: 20rpx;
  background: #ffffff;
  border-radius: 30rpx;
  padding: 30rpx 30rpx 15rpx;
  display: flex;
  .left-record {
    width: 49%;
    .flex-re {
      display: flex;
      align-items: center;
      height: 50rpx;
      line-height: 10rpx;
      img {
        width: 45rpx;
      }
      .collect {
        margin-left: 7rpx;
        font-size: 26rpx;
        font-weight: 600;
      }
      .num {
        font-size: 22rpx;
      }
    }
    .collect-img {
      margin-top: 20rpx;
      display: flex;
      justify-content: space-around;
      img {
        width: 150rpx;
        border-radius: 20rpx;
        background: rgb(246, 247, 251);
      }
    }
  }
  .vertical-line {
    width: 1rpx;         /* 设置线的宽度 */
    height: 150rpx;      /* 设置线的高度 */
    background-color: rgb(229, 229, 229); /* 设置线的颜色 */
    margin: 70rpx 20rpx 5rpx;
  }
  .right-record {
    width: 49%;
    .flex-re {
      display: flex;
      align-items: center;
      height: 50rpx;
      line-height: 10rpx;
      img {
        width: 45rpx;
      }
      .record {
        margin-left: 7rpx;
        font-size: 26rpx;
        font-weight: 600;
        display: inline;
        color: #3F536E;
      }
    }
    .collect-img {
      margin-top: 20rpx;
      display: flex;
      justify-content: space-around;
      img {
        width: 150rpx;
        border-radius: 20rpx;
        background: rgb(246, 247, 251);
      }
    }
  }
}
.information {
  margin-top: 20rpx;
  background: #ffffff;
  border-radius: 30rpx;
  padding: 30rpx 80rpx 30rpx;
  display: flex;
  justify-content: space-between;
  .component {
    font-size: 30rpx;
  }
  .number {
    display: flex;
    align-items: end;
    height: 60rpx;
    line-height: 60rpx;
    .number-title {
      margin-left: 5rpx;
      font-size: 24rpx;
      color: rgb(96, 100, 106);
    }
  }
  .discounts {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .title {
    font-size: 20rpx;
    font-weight: 600;
  }
}



</style>
