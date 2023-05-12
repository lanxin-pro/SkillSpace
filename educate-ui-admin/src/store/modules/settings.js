import defaultSettings from '@/settings'
const { sideTheme, showSettings, topNav, tagsView, fixedHeader, sidebarLogo, dynamicTitle } = defaultSettings
const storageSetting = JSON.parse(localStorage.getItem('layout-setting')) || ''

export default {
    // 指定当前文件作为命名空间
    namespaced: true,
    // 定义全局状态管理的响应式数据
    state() {
        return {
            title: '',
            theme: storageSetting.theme || '#409EFF',
            sideTheme: storageSetting.sideTheme || sideTheme,
            showSettings: showSettings,
            topNav:  storageSetting.topNav === undefined ? topNav : storageSetting.topNav,
            tagsView: storageSetting.tagsView === undefined ? tagsView : storageSetting.tagsView,
            fixedHeader: storageSetting.fixedHeader === undefined ? fixedHeader : storageSetting.fixedHeader,
            sidebarLogo: storageSetting.sidebarLogo === undefined ? sidebarLogo : storageSetting.sidebarLogo,
            dynamicTitle: storageSetting.dynamicTitle === undefined ? dynamicTitle : storageSetting.dynamicTitle
        }
    },
    // 定义修改状态管理的方法
    // 结论：修改state的全局状态数据只能通过mutations的方法进行修改
    // 调用通过：store.commit方法执行和调用
    mutations: {
        CHANGE_SETTING: (state, { key, value }) => {
            if (state.hasOwnProperty(key)) {
                state[key] = value
            }
        }
    },
    // 异步修改状态的的方法
    // 注意：actions定义的方式，不能直接修改state的状态数据
    // 只能通过context.commit去条件mutations的方法去修改state的数据。它是一个间接的方式
    // 调用通过：store.dispatch方法执行和调用
    actions: {
        // 修改布局设置
        changeSetting({ commit }, data) {
            commit('CHANGE_SETTING', data)
        },
        // 设置网页标题
        setTitle({ commit }, title) {
            state.title = title
        }
    },

    // 对state数据的改造和加工处理。给未来的页面和组件进行调用。
    // 从而达到一个封装的目录
    getters: {
        getSidebarLogo(state){
            return state.sidebarLogo
        }
    }


}
