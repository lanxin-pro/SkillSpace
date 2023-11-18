<template>
  <view class="content">
    <view class="content-left">
      <view>asdadsad</view>
      <!-- 使用 v-for 进行列表渲染，:key 提供唯一键 -->
      <block v-for="(item, index) in list" :key="item.id">
        <!-- 使用 v-if 进行条件渲染 -->
        <view v-if="index % 2 === 0" class="content-item">
          <!-- 数据绑定使用 v-bind 或简写 : -->
          <image :src="item.url" class="content-item-img" mode="widthFix" />
          <view class="content-item-box">
            <view class="content-item-title">{{ item.title }}</view>
            <view class="content-item-name">
              <view class="flex-row">
                <image :src="item.avatar" mode="aspectFill" class="content-item-avatar" />
                <view>{{ item.name }}</view>
              </view>
              <view class="content-item-heart">{{ item.num }}</view>
            </view>
          </view>
        </view>
      </block>
    </view>
    <view class="content-right">
      <block v-for="(item, index) in list" :key="item.id">
        <view v-if="index % 2 === 1" class="content-item">
          <!-- 同上，复制左侧的内容 -->
          <!-- 数据绑定使用 v-bind 或简写 : -->
          <image :src="item.url" class="content-item-img" mode="widthFix" />
          <view class="content-item-box">
            <view class="content-item-title">{{ item.title }}</view>
            <view class="content-item-name">
              <view class="flex-row">
                <image :src="item.avatar" mode="aspectFill" class="content-item-avatar" />
                <view>{{ item.name }}</view>
              </view>
              <view class="content-item-heart">{{ item.num }}</view>
            </view>
          </view>
        </view>
      </block>
    </view>
  </view>
</template>


<script>
	export default {
		data() {
			return {
        list: [
          {
            name: '苏苏',
            num: '10',
            title: '虞美人有大长腿',
            url: 'https://img1.baidu.com/it/u=610726533,1686239110&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=750',
            avatar: 'https://img1.baidu.com/it/u=610726533,1686239110&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=750'
          },
          {
            name: '苏苏6',
            num: '8',
            title: '苏苏哇哈哈-苏苏的bug-苏苏小苏苏',
            url: 'https://img0.baidu.com/it/u=1555018837,2431012166&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500',
            avatar: 'https://img0.baidu.com/it/u=1555018837,2431012166&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500'
          },
          {
            name: '苏苏2',
            num: '12',
            title: '你好旧时光',
            url: 'https://img2.baidu.com/it/u=2656232546,4221523240&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=777',
            avatar: 'https://img2.baidu.com/it/u=2656232546,4221523240&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=777'
          },
          {
            name: '苏苏3',
            num: '99+',
            title: '黑暗荣耀',
            url: 'https://img1.baidu.com/it/u=2602506082,2229028687&fm=253&fmt=auto&app=138&f=JPEG?w=796&h=500',
            avatar: 'https://img1.baidu.com/it/u=2602506082,2229028687&fm=253&fmt=auto&app=138&f=JPEG?w=796&h=500'
          },
          {
            name: '苏苏4',
            num: '66',
            title: '归路',
            url: 'https://img1.baidu.com/it/u=3462460603,3220043498&fm=253&fmt=auto&app=120&f=JPEG?w=690&h=458',
            avatar: 'https://img1.baidu.com/it/u=3462460603,3220043498&fm=253&fmt=auto&app=120&f=JPEG?w=690&h=458'
          },
          {
            name: '苏苏5',
            num: '8',
            title: '花琉璃',
            url: 'https://img0.baidu.com/it/u=3184989322,3773788620&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=667',
            avatar: 'https://img0.baidu.com/it/u=3184989322,3773788620&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=667'
          },
        ]
			}
		},
		onLoad() {

		},
		methods: {

		}
	}
</script>

<style>
page {
  padding: 10rpx;
  box-sizing: border-box;
}

.content {
  display: flex;
  justify-content: space-between;
}

.content-left, .content-right {
  width: 49%;
}

.content-item {
  margin: 10rpx auto 20rpx;
  background: #fff;
  width: 98%;
  border-radius: 20rpx;
  overflow: hidden;
}

.content-item-img {
  width: 100%;
}

.content-item-box {
  font-size: 24rpx;
  color: #333;
  padding: 20rpx;
  box-sizing: border-box;
}

.content-item-title {
  /* 一行省略 */
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
  overflow: hidden;
}

.content-item-name {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 20rpx;
  font-size: 22rpx;
}

.content-item-avatar {
  width: 40rpx;
  height: 40rpx;
  border-radius: 50%;
  margin-right: 15rpx;
}

.content-item-heart {
  color: #c4c4c4;
  text-decoration: underline;
  font-size: 26rpx;
}
</style>
