import { defineStore } from 'pinia'

// 这里我们使用的是es6 的模块化规范进行导出的。

// defineStore 方法有两个参数，第一个参数是模块化名字（也就相当于身份证一样，不能重复）

// 第二个参数是选项，对象里面有三个属性，相比于vuex 少了一个 mutations.
export const usePermissionStore = defineStore('admin-user', {
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
        getRouters() {
            return this.routes
        },
    },
    actions: { // 可以通过actions 方法，改变 state 里面的值。
        setSocialLogin(socialType){
            return this.socialLoginType = socialType
        },
        getSocialLogin(){
            return this.socialLoginType;
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

    }
})
