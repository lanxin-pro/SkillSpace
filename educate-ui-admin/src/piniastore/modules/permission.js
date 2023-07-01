import { defineStore } from 'pinia'
import { getRouters } from '@/api/menu/menu.js'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'
import { toCamelCase } from "@/utils"
import Layout from '@/frame/Index.vue'
import ParentView from '@/components/ParentView/index.vue'
import { constantRoutes } from '@/router'
const { wsCache } = useCache()


const loadView = (view) => {
    if(view != null){
        // 只能让他先加载，然后赋值。之前我的写法报错了（乱码我也不知道是什么问题呐） return ()=>import(`../../views/${view}/index.vue`)
        const route =  `../../views/${view}/index.vue`
        return () => import(route)
    }
}

// 这里我们使用的是es6 的模块化规范进行导出的。

// defineStore 方法有两个参数，第一个参数是模块化名字（也就相当于身份证一样，不能重复）

// 第二个参数是选项，对象里面有三个属性，相比于vuex 少了一个 mutations.
export const usePermissionStore = defineStore('admin-permission', {
    // 开启数据持久化
    persist: true,
    state() {  // 存放的就是模块的变量
        return {
            routes: [],
            addRoutes: [],
            sidebarRouters: [], // 左侧边菜单的路由，被 Sidebar/index.vue 使用
            topbarRouters: [], // 顶部菜单的路由，被 TopNav/index.vue 使用
        }
    },
    getters: { // 相当于vue里面的计算属性，可以缓存数据
        getSidebarRouters(){
            return this.sidebarRouters
        }
    },
    actions: { // 可以通过actions 方法，改变 state 里面的值。
        setSocialLogin(socialType){
            return this.socialLoginType = socialType
        },
        getSocialLogin(){
            return this.socialLoginType
        },
        // 社交登录
        SocialLogin(userInfo) {
            console.log(userInfo)
            const code = userInfo.code
            const state = userInfo.state
            const type = userInfo.type
            return new Promise((resolve, reject) => {
                socialLogin(type, code, state).then(res => {
                    res = res.data
                    // 设置 token
                    setToken(res)
                    resolve()
                }).catch(error => {
                    reject(error)
                })
            })
        },
        GenerateRoutes(){
            return new Promise(async resolve => {


                let response = ''
                if(wsCache.get(CACHE_KEY.ROLE_ROUTERS)){
                    response = wsCache.get(CACHE_KEY.ROLE_ROUTERS)
                }else{
                    // 向后端请求路由数据（菜单）
                    response = await getRouters()
                    console.log("缓存")
                    wsCache.set(CACHE_KEY.ROLE_ROUTERS, response)
                }



                const sdata = JSON.parse(JSON.stringify(response.data)) // 【重要】用于菜单中的数据
                const rdata = JSON.parse(JSON.stringify(response.data)) // 用于最后添加到 Router 中的数据
                const sidebarRoutes = filterAsyncRouter(sdata)
                const rewriteRoutes = filterAsyncRouter(rdata, false, true)
                rewriteRoutes.push({path: '/:path(.*)*', redirect: '/404', hidden: true})
                this.SET_ROUTES(rewriteRoutes)
                this.SET_SIDEBAR_ROUTERS(constantRoutes.concat(sidebarRoutes))
                this.SET_DEFAULT_ROUTES(sidebarRoutes)
                this.SET_TOPBAR_ROUTES(sidebarRoutes)
                resolve(rewriteRoutes)
            })
        },
        SET_ROUTES(routes){
            this.addRoutes = routes
            this.routes = constantRoutes.concat(routes)
        },
        SET_SIDEBAR_ROUTERS(routes){
            console.log("这个重要======================》",routes)
            this.sidebarRouters = routes
        },
        SET_DEFAULT_ROUTES(routes){
            this.defaultRoutes = constantRoutes.concat(routes)
        },
        SET_TOPBAR_ROUTES(routes){
            this.topbarRouters = routes
        },

    }
})

/**
 * 遍历后台传来的路由字符串，转换为组件对象
 * 将加载的menu列表，转换成 route.config 路由配置，最后添加到路由中
 */
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false){
    return asyncRouterMap.filter(route => {
        // 将 ruoyi 后端原有耦合前端的逻辑，迁移到此处
        // 处理 meta 属性
        route.meta = {
            title: route.name,
            icon: route.icon,
            noCache: !route.keepAlive,
        }
        route.hidden = !route.visible
        // 处理 name 属性
        if (route.componentName && route.componentName.length > 0) {
            route.name = route.componentName
        }else {
            // 路由地址转首字母大写驼峰，作为路由名称，适配 keepAlive
            route.name = toCamelCase(route.path, true)
            // 处理三级及以上菜单路由缓存问题，将 path 名字赋值给 name
            if (route.path.indexOf("/") !== -1) {
                const pathArr = route.path.split("/");
                route.name = toCamelCase(pathArr[pathArr.length - 1], true)
            }
        }

        // 处理 component 属性
        if (route.children) { // 父节点
            if (route.parentId === 0) {
                route.component = Layout
            } else {
                console.log("出事了")
                route.component = ParentView
            }
        } else { // 根节点
            console.log("添加的根loadView(route.component)",loadView(route.component))
            route.component = loadView(route.component)
        }
        // filterChildren
        if (type && route.children) {
            route.children = filterChildren(route.children)
        }
        if (route.children != null && route.children && route.children.length) {
            route.children = filterAsyncRouter(route.children, route, type)
            route.alwaysShow = route.alwaysShow !== undefined ? route.alwaysShow  : true
        } else {
            delete route['children']
            delete route['alwaysShow'] // 如果没有子菜单，就不需要考虑 alwaysShow 字段
        }
        return true
    })
}

function filterChildren(childrenMap, lastRouter = false) {
    let children = [];
    childrenMap.forEach((el, index) => {
        if (el.children && el.children.length) {
            if (!el.component && !lastRouter) {
                el.children.forEach(c => {
                    c.path = el.path + '/' + c.path
                    if (c.children && c.children.length) {
                        children = children.concat(filterChildren(c.children, c))
                        return
                    }
                    children.push(c)
                })
                return
            }
        }
        if (lastRouter) {
            el.path = lastRouter.path + '/' + el.path
        }
        children = children.concat(el)
    })
    return children
}

