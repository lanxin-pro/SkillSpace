import { createApp } from 'vue' // 导入vue实例createApp方法，用于创建vue对象
import App from './App.vue'
import ElementPlus from 'element-plus' // 导入ElementPlus模块
import 'element-plus/dist/index.css' // 导入ElementPlus全局样式
import * as ElementPlusIconsVue from '@element-plus/icons-vue' // 导入ElementPlus全局图标库
import router from './router' // 导入router
import store from './store' // 导入store
import { setupStore } from '@/piniastore' // 导入pinia相关操作
import Logger from '@/utils/Logger' // 日志
import '@/plugins/windi.css'
import '@/plugins/animate.css' // 引入动画
import '@/plugins/svgIcon' // 引入icon
import '@/styles/index.scss' // 引入全局样式
import '@/assets/styles/index.scss' // 引入全局样式
import 'nprogress/nprogress.css' // 导入进度条样式动画
import './permission.js' // 权限控制
import plugins from './plugins' // plugins
Logger.prettyPrimary(`欢迎使用`, import.meta.env.VITE_APP_TITLE)





const app = createApp(App)
setupStore(app)
app.use(ElementPlus)
app.use(router)
app.use(store)
app.use(ElementPlus) // 使用ElementPlus
app.use(plugins) // plugins
// 注册element-plus所有图标组件
for(const [key,component] of Object.entries(ElementPlusIconsVue)){
    app.component(key,component)
}
app.mount('#app')
