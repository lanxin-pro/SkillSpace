import request from '@/utils/request.js'

/**
 * 查询会员用户列表
 *
 * @param params
 * @returns {*}
 */
export const getUserPage = (params) => {
    return request({
        url: '/member/user/page',
        method: 'get',
        params
    })
}
