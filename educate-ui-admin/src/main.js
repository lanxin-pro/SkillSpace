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
import '@/styles/ruoyi.scss' // 139的版本未发现此内容生效
import '@/plugins/animate.css' // 引入动画
import '@/styles/index.scss' // 引入全局样式
import '@/assets/styles/index.scss' // 引入全局样式
import 'nprogress/nprogress.css' // 导入进度条样式动画
import '@/assets/styles/scrollbar.scss' // 引入滚动条样式
import uploader from 'vue-simple-uploader' // 文件分页上传插件
import 'vue-simple-uploader/dist/style.css' // 文件分页上传插件样式
import './permission.js' // 权限控制
import plugins from './plugins' // plugins
Logger.prettyPrimary(`欢迎使用`, import.meta.env.VITE_APP_TITLE)
// 参考安装文档使用 {@link https://fontawesome.com/docs/web/use-with/vue/}
// 参考文档使用 {@link https://fontawesome.com/docs/web/use-with/vue/add-icons}
import { library } from '@fortawesome/fontawesome-svg-core' /* import the fontawesome core */
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome' /* import font awesome icon component */
import { fas } from '@fortawesome/free-solid-svg-icons' /* import specific icons */ // fas 这个库可以导入多个
import { fab } from '@fortawesome/free-brands-svg-icons' /* import specific icons */ // fab 这个库可以导入多个
import { far } from '@fortawesome/free-regular-svg-icons' /* import specific icons */ // fab 这个库可以导入多个
library.add(fas,fab,far) /* add icons to the library */

// 关闭特定类型的警告
console.warn = () => {}

const app = createApp(App)
    .component('font-awesome-icon', FontAwesomeIcon)
setupStore(app) // pinia
app.use(ElementPlus)
app.use(router)
app.use(store)
app.use(uploader)
app.use(ElementPlus) // 使用ElementPlus
app.use(plugins) // plugins
// 注册element-plus所有图标组件
for(const [key,component] of Object.entries(ElementPlusIconsVue)){
    app.component(key,component)
}
app.mount('#app')
