import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate' // 持久化组件

const store = createPinia()

export const setupStore = (app) => {
    app.use(store)
    store.use(piniaPluginPersistedstate) // 这个有点坑，对我要用pinia去use而不是vue的全局绑定插件
}

export { store }
