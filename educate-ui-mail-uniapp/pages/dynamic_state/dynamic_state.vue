<template>
	<view>
		<view style="position: relative;">
			<swiper @change="handleChange" class="swiper">
			  <swiper-item>
					<view v-for="(item, index) in contentList" :key="index" class="swiper-item">
						<view class="content-box">
						  {{ item.text }}
						</view>
					</view>
			  </swiper-item>
				<swiper-item>
					<view style="background: powderblue;height: 400rpx;">1</view>
				</swiper-item>
			</swiper>
		</view>
		
	</view>
 
</template>

<script>
export default {
  data() {
    return {
      contentList: [
        { text: '内容1' },
        { text: '内容2' },
        { text: '内容3' }
      ]
    }
  },
  methods: {
    handleChange(e) {
		console.log(e)
      if (e.detail.current === this.contentList.length - 1) {
        this.loadMoreContent();
      }
    },
    loadMoreContent() {
      // 模拟加载更多内容
      const newItems = [
        { text: '内容4' },
        { text: '内容5' },
        { text: '内容6' },
        { text: '内容7' },
        { text: '内容8' },
        { text: '内容9' },
        { text: '内容10' }
      ];
      this.contentList.push(...newItems);
    }
  }
}
</script>

<style>
.swiper {
  height: 150px;
}

.swiper-item {
  display: flex;
  justify-content: center;
  align-items: center;
}

.content-box {
  width: 100%;
  height: 100%;
  background-color: #ddd;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 18px;
}
</style>
