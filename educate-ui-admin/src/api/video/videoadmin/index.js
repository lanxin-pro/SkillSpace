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
