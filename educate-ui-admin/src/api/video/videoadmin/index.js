import request from '@/utils/request.js'

/**
 * 获得视频详情分页
 *
 * @param params
 * @returns {*}
 */
export const getVideoPage = params => {
    return request({
        url: '/video/admin/page',
        method: 'get',
        params
    })
}

/**
 * 获取视频信息详情
 *
 * @param videoId
 * @returns {*}
 */
export function getVideoAdmin(videoId) {
    return request({
        url: '/video/admin/get?id=' + videoId,
        method: 'get'
    })
}

/**
 * 创建视频详情
 *
 * @param data
 * @returns {*}
 */
export function createVideo(data) {
    return request({
        url: '/video/admin/create',
        method: 'post',
        data: data
    })
}

/**
 * 修改视频详细
 *
 * @param data
 * @returns {*}
 */
export function updateVideo(data) {
    return request({
        url: '/video/admin/update',
        method: 'post',
        data: data
    })
}
