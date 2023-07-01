/**
 * 配置浏览器本地存储的方式，可直接存储对象数组。
 */


import WebStorageCache from 'web-storage-cache'

const cacheType = 'localStorage' | 'sessionStorage'

export const CACHE_KEY = {
    USER: 'user',
    ROLE_ROUTERS: 'roleRouters',
}

export const useCache = (type = 'localStorage') => {
    const wsCache = new WebStorageCache({
        storage: type
    })

    return {
        wsCache
    }
}
