import request from '@/utils/request'

/**
 * 查询列表支付应用
 *
 * @param params
 * @returns {*}
 */
export function getAppPage(params) {
    return request({
        url: '/pay/app/page',
        method: 'get',
        params
    })
}

/**
 * 查询详情支付应用
 *
 * @param id
 * @returns {*}
 */
export function getApp(id) {
    return request({
        url: '/pay/app/get?id=' + id,
        method: 'get'
    })
}

/**
 * 新增支付应用
 *
 * @param data
 * @returns {*}
 */
export function createApp(data) {
    return request({
        url: '/pay/app/create',
        method: 'post',
        data
    })
}

/**
 * 修改支付应用
 *
 * @param data
 * @returns {*}
 */
export function updateApp(data) {
    return request({
        url: '/pay/app/update',
        method: 'put',
        data
    })
}

/**
 * 支付应用信息状态修改
 *
 * @param data
 * @returns {*}
 */
export function changeAppStatus(data) {
    return request({
        url: '/pay/app/update-status',
        method: 'put',
        data
    })
}

/**
 * 删除支付应用
 *
 * @param id
 * @returns {*}
 */
export function deleteApp(id) {
    return request({
        url: '/pay/app/delete?id=' + id,
        method: 'delete'
    })
}

/**
 * 获得支付应用列表
 *
 * @returns {*}
 */
export function getAppList() {
    return request({
        url: '/pay/app/list',
        method: 'get'
    })
}
