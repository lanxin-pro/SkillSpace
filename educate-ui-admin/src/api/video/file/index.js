import request from '@/utils/request.js'

/**
 * 查询父子结构的分类信息
 *
 * @returns {*}
 */
export const mergeChunks = (data) => {
    return request({
        url: '/video/file/merge',
        method: 'post',
        data: data
    })
}
