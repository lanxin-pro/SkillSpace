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
