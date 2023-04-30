// vue-router 4.x版本以後才能這樣寫
import { createRouter, createWebHistory } from 'vue-router'
// 首页通用组件
import Frame from '@/frame'

// 定义路由配置规则
const routes = [
    {
        // 系统首页
        path: '/Home',
        component: Frame,
        name: 'home',
        meta: { title: 'home', icon: 'el-icon-s-home' },
        hidden: true,
        children: [
            {
                path: '/Home',
                name: 'index',
                meta: { title: 'home', affix: true },
                component: () => import('@/views/Home/Index.vue')
            }
        ]
    },
    {
        path: '/Login',
        hidden: true,
        component: () => import('@/views/Login/Login.vue'),
    },

]
/**
 * 引入创建路由的方法：createRouter
 * 访问模式1：createWebHashHistory
 * 访问模式2：createWebHistory
 */
const router = createRouter({
    history: createWebHistory(),
    routes
})


export default router
