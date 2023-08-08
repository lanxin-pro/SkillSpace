import request from '@/utils/request'

/**
 * 查询 OAuth2 客户端的列表
 *
 * @param params
 * @returns {*}
 */
export const getOAuth2ClientPage = (params) => {
    return request({
        url: '/system/oauth2-client/page',
        method: 'get',
        params: params
    })
}

/**
 * 新增 OAuth2 客户端
 *
 * @param data
 * @returns {*}
 */
export const createOAuth2Client = (data) => {
    return request({
        url: '/system/oauth2-client/create',
        method: 'post',
        data: data
    })
}

/**
 * 修改 OAuth2 客户端
 *
 * @param data
 * @returns {*}
 */
export const updateOAuth2Client = (data) => {
    return request({
        url: '/system/oauth2-client/update',
        method: 'put',
        data: data
    })
}

/**
 * 删除 OAuth2
 *
 * @param id
 * @returns {*}
 */
export const deleteOAuth2Client = (id) => {
    return request({
        url: '/system/oauth2-client/delete?id=' + id,
        method: 'delete'
    })
}

/**
 * 查询 OAuth2 客户端的详情
 *
 * @param id
 * @returns {*}
 */
export const getOAuth2Client = (id) => {
    return request({
        url: '/system/oauth2-client/get?id=' + id,
        method: 'get'
    })
}


/**
 * 获得客户端的全部ids
 *
 * @returns {*}
 */
export const getClientIdsInterface = () => {
    return request({
        url: '/system/oauth2-client/clientIds',
        method: 'get'
    })
}
