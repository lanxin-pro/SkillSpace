<template>
  <view>动态</view>
</template>

<script>
export default {
  onShow() {
    /* 跳转至tab页面的标识 */
    uni.switchTab({
      url: "/pages/member_purchase/member_purchase"
    });
  },
}
</script>

<style scoped>

</style>
