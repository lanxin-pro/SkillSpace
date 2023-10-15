import { defineStore } from 'pinia'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'
import {store} from "@/piniastore/index.js";
const { wsCache } = useCache()

// 这里我们使用的是es6 的模块化规范进行导出的。

// defineStore 方法有两个参数，第一个参数是模块化名字（也就相当于身份证一样，不能重复）

// 第二个参数是选项，对象里面有三个属性，相比于vuex 少了一个 mutations.
export const useAppStore = defineStore('admin-app', {
    // 开启数据持久化
    persist: true,
    state() {  // 存放的就是模块的变量
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
    getters: { // 相当于vue里面的计算属性，可以缓存数据
        getSidebar(){
            return this.sidebar
        },
        getDevice(){
            return this.device
        }
    },
    actions: { // 可以通过actions 方法，改变 state 里面的值。
        toggleSideBar() {
            if (this.sidebar.hide) {
                return false;
            }
            this.sidebar.opened = !this.sidebar.opened;
            this.sidebar.withoutAnimation = false;
            if (this.sidebar.opened) {
                localStorage.setItem("sidebarStatus", 1);
            } else {
                localStorage.setItem("sidebarStatus", 0);
            }
        },
        closeSideBar({ commit }, { withoutAnimation }) {
            localStorage.setItem("sidebarStatus", 0);
            this.sidebar.opened = false;
            this.sidebar.withoutAnimation = withoutAnimation;
        },
        toggleDevice({ commit }, device) {
            this.device = device;
        },

    }
})

export const useAppStoreWithOut = ()=>{
    return useAppStore(store)
}
