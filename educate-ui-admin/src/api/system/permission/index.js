import request from '@/utils/request.js'

/**
 * 查询角色拥有的菜单权限
 *
 * @returns {*}
 */
export const getRoleMenuList = (roleId) => {
    return request({
        url: '/system/permission/list-role-resources?roleId=' + roleId,
        method: 'get'
    })
}

/**
 * 赋予角色菜单权限
 *
 * @param roleId
 * @returns {*}
 */
export const assignRoleMenu = (data) => {
    return request({
        url: '/system/permission/assign-role-menu',
        method: 'post',
        data: data
    })
}

/**
 * 查询用户拥有的角色数组
 *
 * @param userId
 * @returns {*}
 */
export const getUserRoleList = (userId) => {
    return request({
        url: '/system/permission/list-user-roles?userId=' + userId,
        method: 'get',
        data: userId
    })
}

/**
 * 赋予用户角色
 *
 * @param data
 * @returns {*}
 */
export const assignUserRole = (data) => {
    return request({
        url: '/system/permission/assign-user-role',
        method: 'post',
        data: data
    })
}
