import request from '@/utils/request'

/**
 * 查询 token 列表
 *
 * @param params
 * @returns {*}
 */
export const getAccessTokenPage = (params) => {
    return request({
        url: '/system/oauth2-token/page',
        method: 'get',
        params: params
    })
}

/**
 * 删除 token
 *
 * @param accessToken
 * @returns {*}
 */
export const deleteAccessToken = (accessToken) => {
    return request({
        url: '/system/oauth2-token/delete?accessToken=' + accessToken,
        method: 'delete'
    })
}
