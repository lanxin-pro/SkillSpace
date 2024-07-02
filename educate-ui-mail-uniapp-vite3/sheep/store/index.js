import { createPinia } from 'pinia'
/* 持久化组件 */
import piniaPersist from 'pinia-plugin-persist-uni'

// 自动注入所有pinia模块
const files = import.meta.globEager('./*.js');
const modules = {}
Object.keys(files).forEach((key) => {
    modules[key.replace(/(.*\/)*([^.]+).*/gi, '$2')] = files[key].default
})

export const setupPinia = (app) => {
    // 创建pinia
    const pinia = createPinia()
    // 使用pinia持久化组件
    pinia.use(piniaPersist)

    app.use(pinia)
}

export default (name) => {
    return modules[name]()
}
