import request from '@/utils/request'

/**
 * 查询用户列表
 *
 * @param query
 * @returns {*}
 */
export function listUser(query) {
    return request({
        url: '/system/user/page',
        method: 'get',
        params: query
    })
}

/**
 * 查询用户个人信息
 *
 * @returns {*}
 */
export function getUserProfile() {
    return request({
        url: '/system/user/profile/get',
        method: 'get'
    })
}

/**
 * 查询用户详情
 *
 * @param id
 * @returns {Promise<AxiosResponse<any>>}
 */
export const getUser = (id) => {
    return request({
        url: '/system/user/get?id=' + id,
        method: 'get'
    })
}

/**
 * 新增用户
 *
 * @param data
 * @returns {*}
 */
export const createUser = (data) => {
    return request({
        url: '/system/user/create',
        method: 'post',
        data: data
    })
}

/**
 * 修改用户
 *
 * @param data
 * @returns {*}
 */
export const updateUser = (data) => {
    return request({
        url: '/system/user/update',
        method: 'put',
        data: data
    })
}

/**
 * 删除用户
 *
 * @param id
 * @returns {Promise<AxiosResponse<any>>}
 */
export const deleteUser = (id) => {
    return request({
        url: '/system/user/delete?id=' + id,
        method: 'delete'
    })
}

/**
 * 用户状态修改
 *
 * @param id
 * @param status
 * @returns {*}
 */
export const updateUserStatus = (id, status) => {
    const data = {
        id,
        status
    }
    return request({
        url: '/system/user/update-status',
        method: 'put',
        data: data
    })
}

/**
 * 用户密码重置
 *
 * @returns {*}
 */
export function resetUserPwd(id,password) {
    const data = {
        id,
        password
    }
    return request({
        url: '/system/user/update-password',
        method: 'put',
        data: data
    })
}
