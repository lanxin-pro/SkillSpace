export default {
    // 指定当前文件作为命名空间
    namespaced: true,
    // 定义全局状态管理的响应式数据
    state() {
        return {
            sidebar: {
                opened: localStorage.getItem("sidebarStatus")
                    ? !!+localStorage.getItem("sidebarStatus")
                    : true,
                withoutAnimation: false,
                hide: false,
            },
            device: "desktop",
            size: localStorage.getItem("size") || "medium",
        }
    },
    // 定义修改状态管理的方法
    // 结论：修改state的全局状态数据只能通过mutations的方法进行修改
    // 调用通过：store.commit方法执行和调用
    mutations: {
        TOGGLE_SIDEBAR: (state) => {
            if (state.sidebar.hide) {
                return false;
            }
            state.sidebar.opened = !state.sidebar.opened;
            state.sidebar.withoutAnimation = false;
            if (state.sidebar.opened) {
                localStorage.setItem("sidebarStatus", 1);
            } else {
                localStorage.setItem("sidebarStatus", 0);
            }
        },
        CLOSE_SIDEBAR: (state, withoutAnimation) => {
            localStorage.setItem("sidebarStatus", 0);
            state.sidebar.opened = false;
            state.sidebar.withoutAnimation = withoutAnimation;
        },
        TOGGLE_DEVICE: (state, device) => {
            state.device = device;
        },
        SET_SIZE: (state, size) => {
            state.size = size;
            localStorage.setItem("size", size);
        },
        SET_SIDEBAR_HIDE: (state, status) => {
            state.sidebar.hide = status;
        },
    },
    // 异步修改状态的的方法
    // 注意：actions定义的方式，不能直接修改state的状态数据
    // 只能通过context.commit去条件mutations的方法去修改state的数据。它是一个间接的方式
    // 调用通过：store.dispatch方法执行和调用
    actions: {
        toggleSideBar({ commit }) {
            commit("TOGGLE_SIDEBAR");
        },
        closeSideBar({ commit }, { withoutAnimation }) {
            commit("CLOSE_SIDEBAR", withoutAnimation);
        },
        toggleDevice({ commit }, device) {
            commit("TOGGLE_DEVICE", device);
        },
        setSize({ commit }, size) {
            commit("SET_SIZE", size);
        },
        toggleSideBarHide({ commit }, status) {
            commit("SET_SIDEBAR_HIDE", status);
        },
    },

    // 对state数据的改造和加工处理。给未来的页面和组件进行调用。
    // 从而达到一个封装的目录
    getters: {
        getSidebar(state){
            return state.sidebar
        },
        getDevice(state){
            return state.device
        }
    }


}
