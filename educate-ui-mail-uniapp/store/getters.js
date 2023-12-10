export default {
	// !! 转换为布尔值 存在为true
	isLogin: state => !!state.app.token,
};
