/**
 * Shopro-request
 * @description api模块管理，loading配置，请求拦截，错误处理
 */

import Request from 'luch-request';
import $store from '@/sheep/store';

const options = {
	// 显示操作成功消息 默认不显示
	showSuccess: false,
	// 成功提醒 默认使用后端返回值
	successMsg: '',
	// 显示失败消息 默认显示
	showError: true,
	// 失败提醒 默认使用后端返回信息
	errorMsg: '',
	// 显示请求时loading模态框 默认显示
	showLoading: true,
	// loading提醒文字
	loadingMsg: '加载中',
	// 需要授权才能请求 默认放开
	auth: false,
	// ...
};

// Loading全局实例
let LoadingInstance = {
	target: null,
	count: 0,
};

/**
 * 关闭loading
 */
function closeLoading() {
	if (LoadingInstance.count > 0) LoadingInstance.count--;
	if (LoadingInstance.count === 0) uni.hideLoading();
}

/**
 * @description 请求基础配置 可直接使用访问自定义请求
 */
const http = new Request({
	baseURL: 'http://127.0.0.1:9011/server/',
	timeout: 8000,
	method: 'GET',
	header: {
		Accept: 'text/json',
		'Content-Type': 'application/json;charset=UTF-8'
	},
	// #ifdef APP-PLUS
	sslVerify: false,
	// #endif
	// #ifdef H5
	// 跨域请求时是否携带凭证（cookies）仅H5支持（HBuilderX 2.6.15+）
	withCredentials: false,
	// #endif
	custom: options,
});

/**
 * @description 请求拦截器
 */
http.interceptors.request.use((config) => {
	const token = uni.getStorageSync('token');
	if (token) {
		config.header['Authorization'] = "Bearer " + token;
	}
	// TODO 芋艿：特殊处理
	if (config.url.indexOf('/app-api/') !== -1) {
		console.log('存在Accept')
		config.header['Accept'] = '*/*'
		/* 租户 */
		config.header['tenant-id'] = '1'
		/* 终端 必填字段 */
		config.header['terminal'] = '20'
	}
	return config;
},(error) => {
	return Promise.reject(error);
});

/**
 * @description 响应拦截器
 */
http.interceptors.response.use((response) => {
	console.log('响应拦截器response',response)
	console.log('响应拦截器response.header',response.header)
	// 自动设置登陆令牌
	if (response.header.authorization || response.header.Authorization) {
		console.log('设置登录令牌')
		alert('设置登录令牌')
	}
	// TODO j-sentinel：如果是登录的 API，则自动设置 token
	if (response.data?.data?.accessToken) {
		console.log('response2',response)
		$store('user').setToken(response.data.data.accessToken);
	}

	response.config.custom.showLoading && closeLoading();
	if (response.data.code !== 0) {
		if (response.config.custom.showError) {
			uni.showToast({
				title: response.data.msg || '服务器开小差啦,请稍后再试~',
				icon: 'none',
				mask: true,
			});
		}
		return Promise.resolve(response.data);
	}
	// 成功时的提示
	console.log('成功前的一个提示')
	if (
		( response.data.error === 0 || response.data.code === 0) &&
		( response.data.msg !== '' || response.config.custom.successMsg !== '' ) &&
		response.config.custom.showSuccess
	) {
		console.log('成功提示')
		uni.showToast({
			title: response.config.custom.successMsg || response.data.msg,
			icon: 'none',
		});
	}
	return Promise.resolve(response.data);
}, (error) => {
		console.log('这里是相应拦截器的错误处理', error)
})

const request = (config) => {
	if (config.url[0] !== '/') {
		config.url = '/app-api/' + config.url;
	}
	// TODO 芋艿：额外拼接
	if (config.url.indexOf('/app-api/') >= 0) {
		// 设置接口地址
		// config.url = 'http://api-dashboard.yudao.iocoder.cn' + config.url; // 调用【云端】
		// config.url = 'https://app.test.huizhizao.vip/prod-api' + config.url; // 调用【云端】
		config.url = 'http://127.0.0.1:9011/server' + config.url; // 调用【本地】
    // config.url = 'http://yunai.natapp1.cc' + config.url; // 调用【natapp】
	}
	return http.middleware(config);
};

export default request;
