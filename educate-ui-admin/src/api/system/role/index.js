import request from '@/utils/request'

/**
 * 查询角色列表
 *
 * @param params
 * @returns {*}
 */
export const getRolePage = (params) => {
    return request({
        url: '/system/role/page',
        method: 'get',
        params: params,
    })
}

/**
 * 查询角色（精简)列表
 *
 * @returns {*}
 */
export function getSimpleRoleList() {
    return request({
        url: '/system/role/list-all-simple',
        method: 'get'
    })
}

/**
 * 查询角色详细
 *
 * @param roleId
 * @returns {*}
 */
export function getRole(roleId) {
    return request({
        url: '/system/role/get?id=' + roleId,
        method: 'get'
    })
}

/**
 * 新增角色
 *
 * @param data
 * @returns {*}
 */
export function createRole(data) {
    return request({
        url: '/system/role/create',
        method: 'post',
        data: data
    })
}

/**
 * 修改角色
 *
 * @param data
 * @returns {*}
 */
export function updateRole(data) {
    return request({
        url: '/system/role/update',
        method: 'put',
        data: data
    })
}

/**
 * 角色状态修改
 *
 * @param id
 * @param status
 * @returns {*}
 */
export function updateRoleStatus(id, status) {
    const data = {
        id,
        status
    }
    return request({
        url: '/system/role/update-status',
        method: 'put',
        data: data
    })
}

/**
 * 删除角色
 *
 * @param roleId
 * @returns {*}
 */
export function deleteRole(roleId) {
    return request({
        url: '/system/role/delete?id=' + roleId,
        method: 'delete'
    })
}
