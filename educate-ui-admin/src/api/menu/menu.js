import request from '@/utils/request.js'

/**
 * 获取路由
 *
 * @returns {*}
 */
export const getRouters = () => {
    return request({
        url: '/system/auth/list-menus',
        method: 'get'
    })
}
