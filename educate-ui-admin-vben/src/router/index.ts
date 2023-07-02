import type { RouteRecordRaw } from 'vue-router'
import type { App } from 'vue'

import { createRouter, createWebHistory } from 'vue-router'
import { basicRoutes } from './routes'


// app router
// 创建一个可以被 Vue 应用程序使用的路由实例
export const router = createRouter({
    // 这个选项定义了应用程序部署后的基路径。在大多数情况下，我们希望将应用程序部署到 Web 服务器的根目录下
    // 因此该选项值为 "/"。如果应用程序被部署到其他位置，则需要修改它的值
    base: import.meta.env.VITE_APP_APP_NAME ? import.meta.env.VITE_APP_APP_NAME : "/",
    // 创建一个 hash 历史记录。
    history: createWebHistory(import.meta.env.VITE_PUBLIC_PATH), // createWebHashHistory
    // 应该添加到路由的初始路由列表。
    routes: basicRoutes as unknown as RouteRecordRaw[],
    // 是否应该禁止尾部斜杠。默认为假
    strict: true,
    scrollBehavior: () => ({ left: 0, top: 0 })
})

// config router
// 配置路由器
export function setupRouter(app: App<Element>) {
    app.use(router)
}
