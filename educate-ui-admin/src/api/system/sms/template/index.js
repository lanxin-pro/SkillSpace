import request from '@/utils/request'

/**
 * 创建短信模板
 *
 * @param data
 * @returns {*}
 */
export function createSmsTemplate(data) {
    return request({
        url: '/system/sms-template/create',
        method: 'post',
        data: data
    })
}

/**
 * 更新短信模板
 *
 * @param data
 * @returns {*}
 */
export function updateSmsTemplate(data) {
    return request({
        url: '/system/sms-template/update',
        method: 'put',
        data: data
    })
}

/**
 * 删除短信模板
 *
 * @param id
 * @returns {*}
 */
export function deleteSmsTemplate(id) {
    return request({
        url: '/system/sms-template/delete?id=' + id,
        method: 'delete'
    })
}

/**
 * 获得短信模板
 *
 * @param id
 * @returns {*}
 */
export function getSmsTemplate(id) {
    return request({
        url: '/system/sms-template/get?id=' + id,
        method: 'get'
    })
}

/**
 * 获得短信模板分页
 *
 * @param query
 * @returns {*}
 */
export function getSmsTemplatePage(query) {
    return request({
        url: '/system/sms-template/page',
        method: 'get',
        params: query
    })
}

/**
 * 发送测试短信
 *
 * @param data
 * @returns {*}
 */
export function sendSms(data) {
    return request({
        url: '/system/sms-template/send-sms',
        method: 'post',
        data: data
    })
}

