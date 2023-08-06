import request from '@/utils/request'

/**
 * 查询字典列表
 *
 * @returns {AxiosPromise}
 */
export function getDictTypePage(params) {
    return request({
        url: '/system/dict-type/page',
        method: 'get',
        params: params
    })
}

/**
 * 查询字典详情
 *
 * @param id
 * @returns {*}
 */
export function getDictType(id) {
    return request({
        url: '/system/dict-type/get?id=' + id,
        method: 'get'
    })
}

/**
 * 删除字典
 *
 * @param id
 * @returns {*}
 */
export function deleteDictType(id) {
    return request({
        url: '/system/dict-type/delete?id=' + id,
        method: 'delete'
    })
}

/**
 * 新增字典
 *
 * @param data
 * @returns {*}
 */
export function createDictType(data) {
    return request({
        url: '/system/dict-type/create',
        method: 'post',
        data: data
    })
}

/**
 * 修改字典
 *
 * @param data
 * @returns {*}
 */
export function updateDictType(data) {
    return request({
        url: '/system/dict-type/update',
        method: 'put',
        data: data
    })
}
