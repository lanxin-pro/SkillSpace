import request from '@/utils/request'

/**
 * 获取redis 监控信息
 *
 * @param query
 * @returns {*}
 */
export function getCache(query) {
    return request({
        url: '/infra/redis/get-monitor-info',
        method: 'get',
        params: query
    })
}
