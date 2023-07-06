import request from '@/utils/request'

/**
 * 获取岗位精简信息列表
 *
 * @returns {*}
 */
export const listSimplePosts = () => {
    return request({
        url: '/system/post/list-all-simple',
        method: 'get'
    })
}
