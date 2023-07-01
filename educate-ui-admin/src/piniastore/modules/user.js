import { defineStore } from 'pinia'
import { socialLogin,getInfo } from '@/api/login'
import { setToken, removeToken } from '@/utils/auth.js'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'

const { wsCache } = useCache()

// 这里我们使用的是es6 的模块化规范进行导出的。

// defineStore 方法有两个参数，第一个参数是模块化名字（也就相当于身份证一样，不能重复）

// 第二个参数是选项，对象里面有三个属性，相比于vuex 少了一个 mutations.
export const useUserStore = defineStore('admin-user', {
    // 开启数据持久化
    persist: true,
    state() {  // 存放的就是模块的变量
        return {
            socialLoginType: '',
            roles: [],
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
        }
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
        GetInfo() {
            return new Promise(async (resolve,reject)=>{
                try {
                    let response = await getInfo()
                    // 没有 data 数据，赋予个默认值
                    if(!response){
                        response = {
                            data: {
                                roles: [],
                                user: {
                                    id: '',
                                    avatar: '',
                                    userName: '',
                                    nickname: ''
                                }
                            }
                        }
                    }
                    // 读取 data 数据
                    response = response.data
                    // 如何数据库没有头像就赋予默认值
                    response.user.avatar = ( response.user.avatar === "" || response.user.avatar == null )
                        ? import("@/assets/imgs/profile.jpg") : response.user.avatar
                    // 验证返回的roles是否是一个非空数组
                    if (response.roles && response.roles.length > 0) {
                        this.roles = response.roles
                        this.user = response.user
                        wsCache.set(CACHE_KEY.USER,response)
                    }
                    resolve(response)
                }catch (error){
                    reject(error)
                }
            })
        },

    }
})
