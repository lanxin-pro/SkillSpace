import request from '@/utils/request'
import qs from 'qs'

/**
 * 获得我的站内信分页
 *
 * @param query
 * @returns {*}
 */
export function getNotifyMessagePage(query) {
    return request({
        url: '/system/notify-message/page',
        method: 'get',
        params: query
    })
}

/**
 * 获得我的站内信分页
 *
 * @param query
 * @returns {*}
 */
export function getMyNotifyMessagePage(query) {
    return request({
        url: '/system/notify-message/my-page',
        method: 'get',
        params: query
    })
}

/**
 * 批量标记已读
 *
 * @param ids
 * @returns {*}
 */
export function updateNotifyMessageRead(ids) {
    return request({
        url: '/system/notify-message/update-read?' + qs.stringify({ids: ids}, { indices: false }),
        method: 'put'
    })
}

/**
 * 标记所有站内信为已读
 *
 * @returns {*}
 */
export function updateAllNotifyMessageRead() {
    return request({
        url: '/system/notify-message/update-all-read',
        method: 'put'
    })
}

/**
 * 获取当前用户的最新站内信列表
 *
 * @returns {*}
 */
export function getUnreadNotifyMessageList() {
    return request({
        url: '/system/notify-message/get-unread-list',
        method: 'get'
    })
}

/**
 * 获得当前用户的未读站内信数量
 *
 * @returns {*}
 */
export function getUnreadNotifyMessageCount() {
    return request({
        url: '/system/notify-message/get-unread-count',
        method: 'get'
    })
}
