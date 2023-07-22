import request from '@/utils/request'

/**
 * 查询菜单列表
 *
 * @param query
 * @returns {*}
 */
export const listMenu = (query) => {
    return request({
        url: '/system/menu/list',
        method: 'get',
        params: query
    })
}

/**
 * 查询菜单详细
 *
 * @param id
 * @returns {*}
 */
export function getMenu(id) {
    return request({
        url: '/system/menu/get?id=' + id,
        method: 'get'
    })
}

/**
 * 修改菜单
 *
 * @param data
 * @returns {*}
 */
export function updateMenu(data) {
    return request({
        url: '/system/menu/update',
        method: 'put',
        data: data
    })
}

/**
 * 删除菜单
 *
 * @param id
 * @returns {*}
 */
export function deleteMenu(id) {
    return request({
        url: '/system/menu/delete?id=' + id,
        method: 'delete'
    })
}


/**
 * 查询菜单（精简)列表
 *
 * @returns {*}
 */
export function getSimpleMenusList() {
    return request({
        url: '/system/menu/list-all-simple',
        method: 'get'
    })
}

/**
 * 新增菜单
 *
 * @param data
 * @returns {*}
 */
export function createMenu(data) {
    return request({
        url: '/system/menu/create',
        method: 'post',
        data: data
    })
}
