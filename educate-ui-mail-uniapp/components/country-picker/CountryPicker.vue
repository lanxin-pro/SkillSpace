<template>
	 <view>
	    <view class="country-select" @click="show = true">
	      <image class="flag" :src="require('./flags/' + selectedCountryKey.toLowerCase() + '.png')" mode="aspectFit" />
	      <text>{{selectedCountryData.code}}</text>
	    </view>
	    <view class="overlay" v-show="show" @click="show = false">
	      <view class="country-list" @click.stop>
	        <view class="country-item" v-for="(country, key) in countryData" :key="key" @click="selectCountry(key)">
	          <text>{{country.name}} {{country.code}}</text>
	        </view>
	      </view>
	    </view>
	  </view>
</template>

<script>
	import countries from './countries.json';
	export default {
		name: 'CountryPicker',
		data() {
			return {
				show: false,
				selectedCountryKey: 'CN',
				// 这里只列举了一些示例，你需要添加更多的国家和对应的电话区号和语言
				countryData: countries
			}
		},
		computed: {
			selectedCountryData() {
				return this.countryData[this.selectedCountryKey] || {
					name: '',
					code: ''
				};
			}
		},
		methods: {
			selectCountry(key) {
			      this.selectedCountryKey = key;
			      this.show = false;
			      this.$emit('change', this.selectedCountryData);
			    }
		}
	}
</script>

<style scoped>
/* 	.country-picker {
		width: 134rpx;
		height: 100rpx;
		line-height: 100rpx;
		border: 1px solid #ddd;
		border-radius: 10rpx;
		padding: 0 20rpx;
	}

	.picker {
		width: 134rpx;
		height: 100rpx;
		display: flex;
		align-items: center;
	}

	.flag {
		width: 40rpx;
		height: 40rpx;
		margin-right: 10rpx;
	} */
	.country-select {
	  display: flex;
	  align-items: center;
	}

	.flag {
	  width: 24px;
	  height: 24px;
	  margin-right: 7rpx;
	}

	.overlay {
		z-index: 99999;
	  position: fixed;
	  top: 0;
	  left: 0;
	  right: 0;
	  bottom: 0;
	  background: rgba(0, 0, 0, 0.5);
	  display: flex;
	  justify-content: center;
	  align-items: center;
	}

	.country-list {
	  background: #fff;
	  width: 80%;
	  max-height: 80%;
	  overflow-y: scroll;
	  border-radius: 10px;
	}

	.country-item {
	  display: flex;
	  align-items: center;
	  padding: 10px;
	  border-bottom: 1px solid #eee;
	}

	.country-item:last-child {
	  border-bottom: none;
	}
</style>
