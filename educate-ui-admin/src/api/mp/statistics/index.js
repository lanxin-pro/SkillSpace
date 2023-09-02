import request from '@/utils/request'

/**
 * 获取消息发送概况数据
 *
 * @param query
 * @returns {*}
 */
export function getUpstreamMessage(query) {
    return request({
        url: '/mp/statistics/upstream-message',
        method: 'get',
        params: query
    })
}

/**
 * 用户增减数据
 *
 * @param query
 * @returns {*}
 */
export function getUserSummary(query) {
    return request({
        url: '/mp/statistics/user-summary',
        method: 'get',
        params: query
    })
}

/**
 * 获得用户累计数据
 *
 * @param query
 * @returns {*}
 */
export function getUserCumulate(query) {
    return request({
        url: '/mp/statistics/user-cumulate',
        method: 'get',
        params: query
    })
}

/**
 * 获得接口分析数据
 *
 * @param query
 * @returns {AxiosPromise}
 */
export function getInterfaceSummary(query) {
    return request({
        url: '/mp/statistics/interface-summary',
        method: 'get',
        params: query
    })
}
