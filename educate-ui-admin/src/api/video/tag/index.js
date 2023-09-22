import request from '@/utils/request.js'

/**
 * 查询父子结构的分类信息
 *
 * @returns {*}
 */
export const loadDataTag = (query) => {
    return request({
        url: '/video/tag/page',
        method: 'get',
        params: query
    })
}
