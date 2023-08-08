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
