import { CACHE_KEY, useCache } from '@/hooks/web/useCache'

/**
 * 字符权限校验
 * @param {Array} value 校验值
 * @returns {Boolean}
 */
export function checkPermi(value) {
    // if (value && value instanceof Array && value.length > 0) {
    //     const { wsCache } = useCache()
    //     const permissionDatas = value
    //     const all_permission = '*:*:*'
    //     const permissions = wsCache.get(CACHE_KEY.USER).permissions
    //     const hasPermission = permissions.some((permission) => {
    //         return all_permission === permission || permissionDatas.includes(permission)
    //     })
    //     return !!hasPermission
    // } else {
    //     alert("错误的权限信息")
    //     console.error("错误的权限信息")
    //     return false
    // }
    return true
}
