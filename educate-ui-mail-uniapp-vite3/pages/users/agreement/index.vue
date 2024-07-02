<template>
  <view>
    <view class="box-nav-bar">
      <uni-nav-bar
          background-color="#ffffff"
          left-icon="left"
          right-icon="cart"
          title="沐智课堂用户使用协议"
          size="22"
          :border="false"
      >
        <template v-slot:left>
          <text @click="clickLift" class="iconfont icon-xiangzuo"></text>
        </template>
        <template v-slot:right>
          <view></view>
        </template>
      </uni-nav-bar>
    </view>

    <view class="container">
      <view class="title">
        沐智课堂用户使用协议
      </view>
      <view class="timer">
        <view>
          更新时间：2023年9月5日
        </view>
        <view>
          生效时间：2023年9月12日
        </view>
      </view>

      <view>
        <view class="mtm10">【首部及导言】</view>

        <view class="mtm10">
          《沐智课堂弹幕网用户使用协议》（以下简称“本协议”）是您与沐智课堂之间关于使用沐智课堂各项服务的法律协议。“沐智课堂”是指沐智课堂弹幕网的运营公司上海宽娱数码科技有限公司及其关联公司。
        </view>
        <view class="font-weight-bold mtm10">
          本协议中与您的权益（可能）存在重大关系的条款（包括免除沐智课堂责任的条款、限制您权利的条款、争议解决条款及司法管辖条款等），沐智课堂已采用字体加粗等方式来特别提醒您，请您留意重点查阅。请您确保您在使用沐智课堂服务前已仔细阅读、充分理解并毫无保留地接受本协议所有条款。
        </view>
        <view class="mtm10">
          <navigator url="/pages/goods_search/index" class="input">
            《沐智课堂隐私政策》
          </navigator>
          <navigator url="/pages/goods_search/index" class="input">
            《帮助中心》
          </navigator>
          <navigator url="/pages/goods_search/index" class="input">
            《社区公约》
          </navigator>
          <navigator url="/pages/goods_search/index" class="input">
            《小黑屋处罚条例》
          </navigator>
          以及沐智课堂公布的其他协议规范、规则、声明、公告、政策等（以下统称“沐智课堂服务条款”）是本协议不可分割的组成部分，您在使用沐智课堂服务时应同样遵守。
          <text class="font-weight-bold">
            您接受并同意本协议的行为即视为您同意并接受沐智课堂服务条款，您接受并同意任一沐智课堂服务条款的行为亦视为您接受并同意本协议。
          </text>
        </view>
        <view class="mtm10 font-weight-bold">
          您确认您具备完全民事权利能力和完全民事行为能力，有能力同意并遵守本协议，并对您本协议项下的全部行为独立承担法律责任。若您不具备前述与您行为相适应的民事行为能力，则应获得法定监护人的知情同意。
        </view>
        <view class="mtm10 font-weight-bold underline-text">
          如您尚未成年，请在法定监护人的陪同下阅读和判断是否同意本协议，请特别注意未成年人条款。未成年人行使和履行本协议项下的权利和义务视为已获得法定监护人的认可。
        </view>
      </view>
    </view>
  </view>

</template>

<script setup>
const clickLift = () => {
  uni.navigateBack({
    delta: 1  // 返回的页面数，如果是1，则返回上一级页面
  })
}
</script>

<style lang="scss" scoped>
.container {
  margin: 20rpx;
  .title {
    font-size: 36rpx;
    font-weight: bold;
    text-align: center;
  }
  .timer {
    margin: 20rpx 0;
    text-align: right;
  }
  navigator {
    display: inline;
  }

}
</style>
