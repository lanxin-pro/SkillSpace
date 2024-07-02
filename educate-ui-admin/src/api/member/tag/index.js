import request from '@/utils/request.js'

/**
 * 查询会员标签列表
 *
 * @param params
 * @returns {*}
 */
export const getMemberTagPage = (params) => {
    return request({
        url: '/member/tag/page',
        method: 'get',
        params
    })
}

/**
 * 查询会员标签详情
 *
 * @param id
 * @returns {*}
 */
export const getMemberTag = (id) => {
    return request({
        url: `/member/tag/get?id=` + id,
        method: 'get'
    })
}

/**
 * 查询会员标签 - 精简信息列表
 *
 * @returns {*}
 */
export const getSimpleTagList = () => {
    return request({
        url: `/member/tag/list-all-simple`,
        method: 'get'
    })
}

/**
 * 新增会员标签
 *
 * @param data
 * @returns {*}
 */
export const createMemberTag = (data) => {
    return request({
        url: '/member/tag/create',
        method: 'post',
        data
    })
}

/**
 * 修改会员标签
 *
 * @param data
 * @returns {*}
 */
export const updateMemberTag = (data) => {
    return request({
        url: '/member/tag/update',
        method: 'put',
        data
    })
}

/**
 * 删除会员标签
 *
 * @param id
 * @returns {*}
 */
export const deleteMemberTag = (id) => {
    return request({
        url: `/member/tag/delete?id=` + id,
        method: 'delete'
    })
}
