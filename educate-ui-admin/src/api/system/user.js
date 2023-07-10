import request from '@/utils/request'

/**
 * 查询用户列表
 *
 * @param query
 * @returns {*}
 */
export function listUser(query) {
    return request({
        url: '/system/user/page',
        method: 'get',
        params: query
    })
}

/**
 * 查询用户个人信息
 */
export function getUserProfile() {
    return request({
        url: '/system/user/profile/get',
        method: 'get'
    })
}
