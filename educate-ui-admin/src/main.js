import { createApp } from 'vue' // 导入vue实例createApp方法，用于创建vue对象
import App from './App.vue'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 狀態管理
// import store from './store'
// 导入router
import router from './router'

// 导入wind
import 'windi.css'
// 导入动画
import 'animate.css'
// 引入全局样式
import '@/styles/index.scss'


const app = createApp(App)
app.use(ElementPlus)
// 渲染路由
app.use(router)
app.mount('#app')
