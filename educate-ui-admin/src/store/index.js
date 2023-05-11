import { createStore } from 'vuex'
import user from './modules/user.js'

// 创建一个新的 store 实例
const store = createStore({
    // 定义全局状态管理的响应式数据
    modules: {
        user
    }

})

// 导出状态管理
export default store;
