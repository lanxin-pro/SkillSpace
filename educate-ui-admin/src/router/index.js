// vue-router 4.x版本以後才能這樣寫
import { createRouter, createWebHistory } from 'vue-router'
import constantRoutes from './modules/remaining.js'

/**
 * 引入创建路由的方法：createRouter
 * 访问模式1：createWebHashHistory
 * 访问模式2：createWebHistory
 */
const router = createRouter({
    base: import.meta.env.VITE_APP_APP_NAME ? import.meta.env.VITE_APP_APP_NAME : "/", // 这个选项定义了应用程序部署后的基路径。在大多数情况下，我们希望将应用程序部署到 Web 服务器的根目录下，因此该选项值为 "/"。如果应用程序被部署到其他位置，则需要修改它的值。
    history: createWebHistory(),
    scrollBehavior: () => ({y: 0}), // 该选项用于设置当切换路由时，页面应该如何滚动的行为。在这里，我们定义了每次路由切换时都将页面滚动到顶部。
    routes: constantRoutes
})


export default router
