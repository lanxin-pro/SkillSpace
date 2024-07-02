import request from '@/utils/request.js'

/**
 * 查询用户分组列表
 *
 * @param params
 * @returns {*}
 */
export const getGroupPage = (params) => {
    return request({
        url: '/member/group/page',
        method: 'get',
        params
    })
}

/**
 * 查询用户分组详情
 *
 * @param id
 * @returns {*}
 */
export const getGroup = (id) => {
    return request({
        url: `/member/group/get?id=` + id,
        method: 'get'
    })
}

/**
 * 新增用户分组
 *
 * @param data
 * @returns {*}
 */
export const createGroup = (data) => {
    return request({
        url: '/member/group/create',
        method: 'post',
        data
    })
}

/**
 * 查询用户分组 - 精简信息列表
 *
 * @returns {*}
 */
export const getSimpleGroupList = () => {
    return request({
        url: '/member/group/list-all-simple',
        method: 'get'
    })
}

/**
 * 修改用户分组
 *
 * @param data
 * @returns {*}
 */
export const updateGroup = (data) => {
    return request({
        url: '/member/group/update',
        method: 'put',
        data
    })
}

/**
 * 删除用户分组
 *
 * @param id
 * @returns {*}
 */
export const deleteGroup = (id) => {
    return request({
        url: `/member/group/delete?id=` + id,
        method: 'delete'
    })
}
