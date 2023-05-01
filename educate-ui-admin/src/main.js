import { createApp } from 'vue' // 导入vue实例createApp方法，用于创建vue对象
import App from './App.vue'
import ElementPlus from 'element-plus' // 导入ElementPlus模块
import 'element-plus/dist/index.css' // 导入ElementPlus全局样式
import router from './router' // 导入router
import Logger from '@/utils/Logger' // 日志
import '@/plugins/windi.css'
import '@/plugins/animate.css' // 引入动画
import '@/styles/index.scss' // 引入全局样式
Logger.prettyPrimary(`欢迎使用`, import.meta.env.VITE_APP_TITLE)





const app = createApp(App)
app.use(ElementPlus)
app.use(router)
app.mount('#app')
