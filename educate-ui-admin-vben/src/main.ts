import 'virtual:windi-base.css'
import 'virtual:windi-components.css'
import '@/design/index.less'
import 'virtual:windi-utilities.css'
import { router,setupRouter } from '@/router'
import Logger from '@/utils/Logger' // 日志
import { createApp } from 'vue'
import App from './App.vue'

import('ant-design-vue/dist/antd.less')



Logger.prettyPrimary(`欢迎使用`, import.meta.env.VITE_GLOB_APP_TITLE)

// 关闭特定类型的警告
console.warn = () => {}

const app = createApp(App)

// Configure routing
// 配置路由
setupRouter(app)

app.mount('#app')
