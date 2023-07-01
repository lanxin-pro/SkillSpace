// 首页通用组件
import Frame from '@/frame/Index.vue'


/**
 * Note: 路由配置项
 *
 * hidden: true                   // 【重要】当设置 true 的时候该路由不会再侧边栏出现 如 401，login 等页面，或者如一些编辑页面 /edit/1
 * alwaysShow: true               // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * path: '/login',                // 【重要】访问的 URL 路径
 * component: Layout,             // 【重要】对应的组件；也可以是 (resolve) => require(['@/views/login'], resolve),
 * redirect: noRedirect           // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'             // 【重要】设定路由的名字，一定要填写不然使用 <keep-alive> 时会出现各种问题
 * meta : {
    noCache: true                // 【重要】如果设置为 true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'               // 【重要】设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'             // 【重要】设置该路由的图标，对应路径 src/assets/icons/svg
    breadcrumb: false            // 如果设置为 false，则不会在 breadcrumb 面包屑中显示
    activeMenu: '/system/user'   // 当路由设置了该属性，则会高亮相对应的侧边栏。
  }
 */

// 定义路由配置规则
const constantRoutes = [
    {
        path: '/Login',
        hidden: true,
        name: 'Login',
        component: (resolve) => import('@/views/Login/Login.vue',resolve),
    },
    {
        path: '/sso',
        // 地址和登录页面一样，但是name不一样，route.name如果是SSOLogin的话，就会触发监听器
        component: (resolve) => import('@/views/Login/Login.vue', resolve),
        name: 'SSOLogin',
        hidden: true
    },
    {
        path: '/social-login',
        component: (resolve) => import('@/views/Login/Login.vue', resolve),
        name: 'socialLogin',
        hidden: true
    },
    {
        path: '/social-login',
        component: (resolve) => import('@/views/socialLogin/Index.vue', resolve),
        hidden: true
    },
    {
        path: '/401',
        component: (resolve) => import('@/views/error/401.vue',resolve),
        hidden: true
    },
    {
        path: '/404',
        component: (resolve) => import('@/views/error/404.vue',resolve),
        hidden: true
    },
    {
        // 系统首页
        path: '/',
        component: Frame,
        children: [
            {
                path: '/Home',
                component: (resolve) => import('@/views/Home/Index.vue',resolve),
                name: '首页',
                meta: { title: '首页', icon: 'fa-home', affix: true }
            }
        ]
    },

]

export default constantRoutes
