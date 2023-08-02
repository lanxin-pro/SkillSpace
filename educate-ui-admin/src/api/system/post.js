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

/**
 * 查询岗位列表
 *
 * @returns {*}
 */
export const getPostPage = (params) => {
    return request({
        url: '/system/post/page',
        method: 'get',
        params: params
    })
}

/**
 * 删除岗位
 *
 * @param id
 * @returns {*}
 */
export const deletePost = (id) => {
    return request({
        url: '/system/post/delete?id=' + id,
        method: 'get',
    })
}

/**
 * 查询岗位详情
 *
 * @param id
 * @returns {*}
 */
export const getPost = (id) => {
    return request({
        url: '/system/post/get?id=' + id,
        method: 'get',
    })
}

/**
 * 新增岗位
 *
 * @param data
 * @returns {*}
 */
export const createPost = (data) => {
    return request({
        url: '/system/post/create',
        method: 'post',
        data: data
    })
}

/**
 * 修改岗位
 *
 * @param data
 * @returns {*}
 */
export const updatePost = (data) => {
    return request({
        url: '/system/post/update',
        method: 'put',
        data: data
    })
}
