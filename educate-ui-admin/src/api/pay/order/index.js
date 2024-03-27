import request from '@/utils/request'

/**
 * 查询列表支付订单
 *
 * @param params
 * @returns {*}
 */
export function getOrderPage(params) {
    return request({
        url: '/pay/order/page',
        method: 'get',
        params
    })
}

/**
 * 查询详情支付订单
 *
 * @param id
 * @returns {*}
 */
export function getOrder(id) {
    return request({
        url: '/pay/order/get?id=' + id,
        method: 'get'
    })
}

/**
 * 获得支付订单的明细
 *
 * @returns {*}
 */
export function getOrderDetail(id) {
    return request({
        url: '/pay/order/get-detail?id=' + id,
        method: 'get'
    })
}

/**
 * 提交支付订单
 *
 * @returns {*}
 */
export function submitOrder(data) {
    return request({
        url: '/pay/order/submit',
        method: 'post',
        data
    })
}

/**
 * 获得支付订单的明细
 *
 * @returns {*}
 */
export function exportOrder(params) {
    return request({
        url: '/pay/order/export-excel',
        method: 'get',
        responseType: 'blob',
        params
    })
}
