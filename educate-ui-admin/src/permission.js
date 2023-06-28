/*
路由规则
// 当设置 true 的时候该路由不会在侧边栏出现 如 401，login 等页面，或者如一些编辑页面 /edit/1
hidden: true // (默认 false)

// 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
redirect: 'noRedirect'

// 1. 当你一个路由下面的 children 声明的路由大于 1 个时，自动会变成嵌套的模式。例如说，组件页面
// 2. 只有一个时，会将那个子路由当做根路由显示在侧边栏。例如说，如引导页面
// 若你想不管路由下面的 children 声明的个数都显示你的根路由，
// 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
alwaysShow: true

name: 'router-name' // 设定路由的名字，一定要填写不然使用 <keep-alive> 时会出现各种问题
meta: {
    roles: ['admin', 'editor'] // 设置该路由进入的权限，支持多个权限叠加
    title: 'title' // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name' // 设置该路由的图标，支持 svg-class，也支持 el-icon-x element-ui 的 icon
    noCache: true // 如果设置为 true，则不会被 <keep-alive> 缓存(默认 false)
    breadcrumb: false //  如果设置为 false，则不会在breadcrumb面包屑中显示(默认 true)
    affix: true // 如果设置为 true，它则会固定在 tags-view 中(默认 false)

    // 当路由设置了该属性，则会高亮相对应的侧边栏。
    // 这在某些场景非常有用，比如：一个文章的列表页路由为：/article/list
    // 点击文章进入文章详情页，这时候路由为 /article/1，但你想在侧边栏高亮文章列表的路由，就可以进行如下设置
    activeMenu: '/article/list'
}

示例
{
  path: '/system/test',
  component: Layout,
  redirect: 'noRedirect',
  hidden: false,
  alwaysShow: true,
  meta: { title: '系统管理', icon : "system" },
  children: [{
    path: 'index',
    component: (resolve) => require(['@/views/index'], resolve),
    name: 'Test',
    meta: {
      title: '测试管理',
      icon: 'user'
    }
  }]
}
*/
import router from './router'
import { useUserStore } from '@/piniastore/modules/user.js'
import { usePermissionStore } from '@/piniastore/modules/permission.js'
import { ElMessage } from 'element-plus'
import { showFullLoading,hideFullLoading } from '@/utils/index.js'
import { getAccessToken } from '@/utils/auth'


// 增加三方登陆 测试方便我添加了sso的白名单
const whiteList = ['/login', '/social-login',  '/auth-redirect', '/bind', '/register', '/oauthLogin/gitee','/sso']

/**
 * to 来的页面
 * from 要去的页面
 * next() 放行
 */
router.beforeEach((to,from,next)=>{
    // 开启动画
    showFullLoading()
    // 获取token
    if(getAccessToken()){
        // has token
        if(whiteList.indexOf(to.path) !== -1){
            next('/')
            hideFullLoading()
        }else{
            // 创建pinia的位置也非常的讲究，不然pinia会NullPointerException
            const userStore = useUserStore()
            if(userStore.getRoles.length === 0){
                const permissionStore = usePermissionStore()
                // 判断当前用户是否已拉取完 user_info 信息
                userStore.GetInfo().then(res => {
                    console.log("开始执行GenerateRoutes")
                    permissionStore.GenerateRoutes().then(res => {

                    })
                }).catch((error) => {

                })
            }
        }
    }else{
        // 没有token
        if (whiteList.indexOf(to.path) !== -1) {
            // 在免登录白名单，直接进入
            hideFullLoading()
            next()
        } else {
            const redirect = encodeURIComponent(to.fullPath) // 编码 URI，保证参数跳转回去后，可以继续带上
            ElMessage({
                message: "很抱歉请您先登录",
                type: 'error'
            })
            next(`/login?redirect=${redirect}`) // 否则全部重定向到登录页
            hideFullLoading()
        }
    }


    next()
})

router.afterEach(() => {
    hideFullLoading()
})
