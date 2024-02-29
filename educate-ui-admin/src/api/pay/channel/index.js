import request from '@/utils/request'

/**
 * 查询列表支付渠道
 *
 * @param params
 * @returns {*}
 */
export function getChannelPage(params) {
    return request({
        url: '/pay/channel/page',
        method: 'get',
        params
    })
}

/**
 * 查询详情支付渠道
 *
 * @param appId
 * @param code
 * @returns {*}
 */
export function getChannel(appId, code) {
    const params = {
        appId: appId,
        code: code
    }
    return request({
        url: '/pay/channel/get',
        method: 'get',
        params
    })
}

/**
 * 新增支付渠道
 *
 * @param data
 * @returns {*}
 */
export function createChannel(data) {
    return request({
        url: '/pay/channel/create',
        method: 'post',
        data
    })
}

/**
 * 修改支付渠道
 *
 * @param data
 * @returns {*}
 */
export function updateChannel(data) {
    return request({
        url: '/pay/channel/update',
        method: 'put',
        data
    })
}

/**
 * 删除支付渠道
 *
 * @param id
 * @returns {*}
 */
export function deleteChannel(id) {
    return request({
        url: '/pay/channel/delete?id=' + id,
        method: 'delete'
    })
}

/**
 * 导出支付渠道
 *
 * @returns {*}
 */
export function exportChannel(params) {
    return request({
        url: '/pay/channel/export-excel',
        method: 'get',
        params
    })
}
