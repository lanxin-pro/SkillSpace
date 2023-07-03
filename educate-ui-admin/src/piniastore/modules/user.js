import { defineStore } from 'pinia'
import { socialLogin,getInfo } from '@/api/login'
import { getAccessToken,setToken, removeToken } from '@/utils/auth.js'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'
const { wsCache } = useCache()

// 这里我们使用的是es6 的模块化规范进行导出的。

// defineStore 方法有两个参数，第一个参数是模块化名字（也就相当于身份证一样，不能重复）

// 第二个参数是选项，对象里面有三个属性，相比于vuex 少了一个 mutations.
export const useUserStore = defineStore('admin-user', {
    // 开启数据持久化
    persist: false,
    state() {  // 存放的就是模块的变量
        return {
            socialLoginType: '',
            roles: [],
            isSetUser: false,
            user: {
                id: '',
                avatar: '',
                userName: '',
                nickname: ''
            }
        }
    },
    getters: { // 相当于vue里面的计算属性，可以缓存数据
        getRoles(){
            return this.roles
        },
        getUser(){
            return this.user
        },
        getIsSetUser() {
            return this.isSetUser
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
        async GetInfo() {
            if (!getAccessToken()) {
                this.resetState()
                return null
            }
            let userInfo = wsCache.get(CACHE_KEY.USER)
            if (!userInfo) {
                userInfo = await getInfo()
            }
            this.permissions = userInfo.data.permissions
            this.roles = userInfo.data.roles
            this.user = userInfo.data.user
            this.isSetUser = true
            wsCache.set(CACHE_KEY.USER, userInfo)
        },
        resetState() {
            this.permissions = []
            this.roles = []
            this.isSetUser = false
            this.user = {
                id: 0,
                avatar: '',
                nickname: ''
            }
        }


    }
})
