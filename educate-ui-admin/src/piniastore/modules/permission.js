import { defineStore } from 'pinia'
import { getRouters } from '@/api/menu/menu.js'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'
import { toCamelCase } from "@/utils"

import ParentView from '@/components/ParentView/index.vue'
import { cloneDeep,omit } from 'lodash-es'
import constantRoutes from '@/router/modules/remaining.js'
import { createRouter, createWebHashHistory } from 'vue-router'

const { wsCache } = useCache()

/* Layout */
export const Layout = () => import('@/frame/Index.vue')

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
            // 目前这个是给tag使用的
            routers: [],
            // 这个路由暂时无用
            routes: [],
            addRoutes: [],
            sidebarRouters: [], // 左侧边菜单的路由，被 Sidebar/index.vue 使用
            topbarRouters: [], // 顶部菜单的路由，被 TopNav/index.vue 使用
        }
    },
    getters: { // 相当于vue里面的计算属性，可以缓存数据
        getSidebarRouters(){
            return this.sidebarRouters
        },
        getAddRouters(){
            return flatMultiLevelRoutes(cloneDeep(this.addRouters))
        },
        getRouters(){
            return this.routers
        },
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
                    wsCache.set(CACHE_KEY.ROLE_ROUTERS, response)
                }

                let routerMap = generateRoute(response.data)
                // 动态路由，404一定要放到最后面
                this.addRouters = routerMap.concat([
                    {
                        path: '/:path(.*)*',
                        redirect: '/404',
                        name: '404Page',
                        meta: {
                            hidden: true,
                            breadcrumb: false
                        }
                    }
                ])
                // 渲染菜单的所有路由  拼接静态路由+动态路由
                this.routers = cloneDeep(constantRoutes).concat(routerMap)
                this.sidebarRouters = cloneDeep(constantRoutes).concat(routerMap)
                resolve()
            })
        },

    }
})

// 查找文件
const modules = import.meta.glob('@/views/**/index.{vue,tsx}')

// 后端控制路由生成
function generateRoute(routes){
    const res = []
    const modulesRoutesKeys = Object.keys(modules)
    for (const route of routes) {
        const meta = {
            title: route.name,
            icon: route.icon,
            hidden: !route.visible,
            noCache: !route.keepAlive,
            alwaysShow:
                route.children &&
                route.children.length === 1 &&
                (route.alwaysShow !== undefined ? route.alwaysShow : true)
        }
        // 路由地址转首字母大写驼峰，作为路由名称，适配keepAlive
        let data = {
            path: route.path,
            name:
                route.componentName && route.componentName.length > 0
                    ? route.componentName
                    : toCamelCase(route.path, true),
            redirect: route.redirect,
            meta: meta
        }
        //处理顶级非目录路由
        if (!route.children && route.parentId == 0 && route.component) {
            data.component = Layout
            data.meta = {}
            data.name = toCamelCase(route.path, true) + 'Parent'
            data.redirect = ''
            meta.alwaysShow = true
            const childrenData = {
                path: '',
                name: toCamelCase(route.path, true),
                redirect: route.redirect,
                meta: meta
            }
            const index = route?.component
                ? modulesRoutesKeys.findIndex((ev) => ev.includes(route.component))
                : modulesRoutesKeys.findIndex((ev) => ev.includes(route.path))
            childrenData.component = modules[modulesRoutesKeys[index]]
            data.children = [childrenData]
        } else {
            // 目录
            if (route.children) {
                data.component = Layout
                data.redirect = getRedirect(route.path, route.children)

            } else {
                // 对后端传component组件路径和不传做兼容（如果后端传component组件路径，那么path可以随便写，如果不传，component组件路径会根path保持一致）
                const index = route?.component
                    ? modulesRoutesKeys.findIndex((ev) => ev.includes(route.component))
                    : modulesRoutesKeys.findIndex((ev) => ev.includes(route.path))
                data.component = modules[modulesRoutesKeys[index]]
            }
            if (route.children) {
                data.children = generateRoute(route.children)
            }
        }
        res.push(data)
    }
    return res
}

function getRedirect(parentPath, children){
    if (!children || children.length == 0) {
        return parentPath
    }
    const path = generateRoutePath(parentPath, children[0].path)
    // 递归子节点
    if (children[0].children) return getRedirect(path, children[0].children)
}
function generateRoutePath(parentPath, path){
    if (parentPath.endsWith('/')) {
        parentPath = parentPath.slice(0, -1) // 移除默认的 /
    }
    if (!path.startsWith('/')) {
        path = '/' + path
    }
    return parentPath + path
}
function flatMultiLevelRoutes(routes){
    const modules = cloneDeep(routes)
    for (let index = 0; index < modules.length; index++) {
        const route = modules[index]
        if (!isMultipleRoute(route)) {
            continue
        }
        promoteRouteLevel(route)
    }
    return modules
}

// 层级是否大于2
function isMultipleRoute(route){
    if (!route || !Reflect.has(route, 'children') || !route.children?.length) {
        return false
    }

    const children = route.children

    let flag = false
    for (let index = 0; index < children.length; index++) {
        const child = children[index]
        if (child.children?.length) {
            flag = true
            break
        }
    }
    return flag
}

// 生成二级路由
function promoteRouteLevel(route){
    let router = createRouter({
        routes: [route],
        history: createWebHashHistory()
    })

    const routes = router.getRoutes()
    addToChildren(routes, route.children || [], route)
    router = null

    route.children = route.children?.map((item) => omit(item, 'children'))
}

// 添加所有子菜单
function addToChildren(routes,children,routeModule){
    for (let index = 0; index < children.length; index++) {
        const child = children[index]
        const route = routes.find((item) => item.name === child.name)
        if (!route) {
            continue
        }
        routeModule.children = routeModule.children || []
        if (!routeModule.children.find((item) => item.name === route.name)) {
            routeModule.children?.push(route)
        }
        if (child.children?.length) {
            addToChildren(routes, child.children, routeModule)
        }
    }
}

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

