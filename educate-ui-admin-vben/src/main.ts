import 'virtual:windi-base.css'
import 'virtual:windi-components.css'
import 'virtual:windi-utilities.css'
import { router,setupRouter } from '@/router'

import { createApp } from 'vue'
import App from './App.vue'


const app = createApp(App)

// Configure routing
// 配置路由
setupRouter(app)

app.mount('#app')
