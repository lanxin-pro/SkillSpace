import { defineStore } from 'pinia'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'
const { wsCache } = useCache()
import { store } from '../index'
import { getRawRoute } from '@/utils/routerHelper.js'

// 这里我们使用的是es6 的模块化规范进行导出的。

// defineStore 方法有两个参数，第一个参数是模块化名字（也就相当于身份证一样，不能重复）

// 第二个参数是选项，对象里面有三个属性，相比于vuex 少了一个 mutations.
export const useTagsViewStore = defineStore('tag-view', {
    // 开启数据持久化
    persist: true,
    state() {  // 存放的就是模块的变量
        return {
            visitedViews: [],
            cachedViews: new Set()
        }
    },
    getters: { // 相当于vue里面的计算属性，可以缓存数据
        getVisitedViews() {
            return this.visitedViews
        },
    },
    actions: { // 可以通过actions 方法，改变 state 里面的值。
        // 新增缓存和tag
        addView(view) {
            this.addVisitedView(view)
            this.addCachedView()
        },
        // 新增tag
        addVisitedView(view) {
            if (this.visitedViews.some((v) => v.path === view.path)) return
            if (view.meta?.noTagsView) return
            this.visitedViews.push(
                Object.assign({}, view, {
                    title: view.meta?.title || 'no-name'
                })
            )
        },
        // 新增缓存
        addCachedView() {
            const cacheMap = new Set()
            for (const v of this.visitedViews) {
                const item = getRawRoute(v)
                const needCache = !item.meta?.noCache
                if (!needCache) {
                    continue
                }
                const name = item.name
                cacheMap.add(name)
            }
            if (Array.from(this.cachedViews).sort().toString() === Array.from(cacheMap).sort().toString())
                return
            this.cachedViews = cacheMap
        },

    }
})

export const useTagsViewStoreWithOut = () => {
    return useTagsViewStore(store)
}

