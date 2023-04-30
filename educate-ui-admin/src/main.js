// 导入vue实例createApp方法，用于创建vue对象
import { createApp } from 'vue'
import App from './App.vue'
// 狀態管理
// import store from './store'
// 导入router
import router from './router'









const app = createApp(App)
// 渲染路由
app.use(router)
app.mount('#app')
