import { login }  from '@/api/login/index.js'

export default {
    // 指定当前文件作为命名空间
    namespaced: true,
    // 定义全局状态管理的响应式数据
    state() {
        return {
            id: 0, // 用户编号
            name: '',
            avatar: '',
            roles: [],
            permissions: []
        }
    },
    // 定义修改状态管理的方法
    // 结论：修改state的全局状态数据只能通过mutations的方法进行修改
    // 调用通过：store.commit方法执行和调用
    mutations: {
        SET_ID: (state, id) => {
            state.id = id
        },
        SET_NAME: (state, name) => {
            state.name = name
        },
        SET_NICKNAME: (state, nickname) => {
            state.nickname = nickname
        },
        SET_AVATAR: (state, avatar) => {
            state.avatar = avatar
        },
        SET_ROLES: (state, roles) => {
            state.roles = roles
        },
        SET_PERMISSIONS: (state, permissions) => {
            state.permissions = permissions
        }
    },
    // 异步修改状态的的方法
    // 注意：actions定义的方式，不能直接修改state的状态数据
    // 只能通过context.commit去条件mutations的方法去修改state的数据。它是一个间接的方式
    // 调用通过：store.dispatch方法执行和调用
    actions: {
        // 登录
        async Login({ commit }, userInfo) {
            const username = userInfo.username.trim()
            const password = userInfo.password
            const captchaVerification = userInfo.captchaVerification
            const socialCode = userInfo.socialCode
            const socialState = userInfo.socialState
            const socialType = userInfo.socialType
            try{
                const serverResponse = await login(username, password, captchaVerification, socialType, socialCode, socialState)
                return Promise.resolve(serverResponse)
            }catch(err){
                return Promise.reject(err)
            }
        },

    },

    // 对state数据的改造和加工处理。给未来的页面和组件进行调用。
    // 从而达到一个封装的目录
    getters: {

    }


}
