import request from '@/utils/request.js'

/**
 * 查询父子结构的分类信息
 *
 * @returns {*}
 */
export const loadDataTree = () => {
    return request({
        url: '/video/category/tree',
        method: 'post',
        contentType: 1
    })
}
