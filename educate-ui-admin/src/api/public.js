import request from '@/utils/request.js'

/**
 * 服务器时间
 *
 * @param params
 * @returns {*}
 */
export const getServerTime = params => {
    return request({
        url: '/systemTime/getServerTime',
        method: 'get',
        params
    })
}
