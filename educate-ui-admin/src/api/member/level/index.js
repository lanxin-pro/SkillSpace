import request from '@/utils/request.js'

/**
 * 查询会员等级分页
 *
 * @returns {*}
 */
export const getLevelPage = (params) => {
    return request({
        url: '/member/level/page',
        method: 'get',
        params
    })
}

/**
 * 查询会员等级列表
 *
 * @returns {*}
 */
export const getLevelList = () => {
    return request({
        url: '/member/level/list',
        method: 'get'
    })
}

/**
 * 查询会员等级详情
 *
 * @param id
 * @returns {*}
 */
export const getLevel = (id) => {
    return request({
        url: `/member/level/get?id=` + id,
        method: 'get'
    })
}

/**
 * 查询会员等级 - 精简信息列表
 *
 * @returns {*}
 */
export const getSimpleLevelList = () => {
    return request({
        url: '/member/level/list-all-simple',
        method: 'get'
    })
}

/**
 * 新增会员等级
 *
 * @param data
 * @returns {*}
 */
export const createLevel = (data) => {
    return request({
        url: '/member/level/create',
        method: 'post',
        data
    })
}

/**
 * 修改会员等级
 *
 * @param data
 * @returns {*}
 */
export const updateLevel = (data) => {
    return request({
        url: '/member/level/update',
        method: 'put',
        data
    })
}


/**
 * 删除会员等级
 *
 * @param id
 * @returns {*}
 */
export const deleteLevel = (id) => {
    return request({
        url: `/member/level/delete?id=` + id,
        method: 'delete'
    })
}
