import request from '@/utils/request'

/**
 * 创建示例转账单
 *
 * @param data
 * @returns {*}
 */
export function createDemoTransfer(data) {
    return request({
        url: '/pay/demo-transfer/create',
        method: 'post',
        data
    })
}

/**
 * 获得示例订单分页
 *
 * @param query
 * @returns {*}
 */
export function getDemoTransferPage(query) {
    return request({
        url: '/pay/demo-transfer/page',
        method: 'get',
        params: query
    })
}
