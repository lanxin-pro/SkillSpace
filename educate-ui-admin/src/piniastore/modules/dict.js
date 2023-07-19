import { defineStore } from 'pinia'
import { store } from '../index.js'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'
import { listSimpleDictData } from '@/api/system/dict/data'

const { wsCache } = useCache()

// 这里我们使用的是es6 的模块化规范进行导出的。

// defineStore 方法有两个参数，第一个参数是模块化名字（也就相当于身份证一样，不能重复）

// 第二个参数是选项，对象里面有三个属性，相比于vuex 少了一个 mutations.
export const useDictStore = defineStore('admin-dict', {
    // 开启数据持久化
    persist: false,
    state() {  // 存放的就是模块的变量
        return {
            dictMap: [],
            isSetDict: false
        }
    },
    getters: { // 相当于vue里面的计算属性，可以缓存数据
        getDictMap() {
            const dictMap = wsCache.get(CACHE_KEY.DICT_CACHE)
            if (dictMap) {
                this.dictMap = dictMap
            }
            return this.dictMap
        },
        getIsSetDict() {
            return this.isSetDict
        }
    },
    actions: { // 可以通过actions 方法，改变 state 里面的值。
        getDictByType(type){
            if (!this.isSetDict) {
                // this.setDictMap()
            }
            return this.dictMap[type]
        },
        async setDictMap() {
            const dictMap = wsCache.get(CACHE_KEY.DICT_CACHE)
            console.log('dictMap',dictMap)
            if (dictMap) {
                this.dictMap = dictMap
                this.isSetDict = true
            } else {
                // 获取全部的数据字典
                const response = await listSimpleDictData()
                const res = response.data
                // 设置数据
                let dictDataMap = new Map()
                res.forEach((dictData) => {
                    // 获得 dictType 层级
                    const enumValueObj = dictDataMap[dictData.dictType]
                    if (!enumValueObj) {
                        dictDataMap[dictData.dictType] = []
                    }
                    // 处理 dictValue 层级
                    dictDataMap[dictData.dictType].push({
                        value: dictData.value,
                        label: dictData.label,
                        colorType: dictData.colorType,
                        cssClass: dictData.cssClass
                    })
                })
                this.dictMap = dictDataMap
                this.isSetDict = true
                // TODO j-sentinel 使用 const dictDataMap = [] 无法存储值，所以这里使用Map
                wsCache.set(CACHE_KEY.DICT_CACHE, dictDataMap)

            }
        }


    }
})


export const useDictStoreWithOut = ()=>{
    return useDictStore(store)
}

