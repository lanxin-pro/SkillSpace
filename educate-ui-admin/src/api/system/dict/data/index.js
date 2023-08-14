import request from '@/utils/request'

/**
 * 查询全部字典数据列表
 *
 * @returns {AxiosPromise}
 */
export function listSimpleDictData() {
    return request({
        url: '/system/dict-data/list-all-simple',
        method: 'get',
    })
}

/**
 * 查询字典数据列表
 *
 * @param query
 * @returns {*}
 */
export function getDictDataPage(query) {
    return request({
        url: '/system/dict-data/page',
        method: 'get',
        params: query
    })
}

/**
 * 查询字典数据详细
 *
 * @param dictCode
 * @returns {*}
 */
export function getDictData(dictCode) {
    return request({
        url: '/system/dict-data/get?id=' + dictCode,
        method: 'get'
    })
}

/**
 * 新增字典数据
 *
 * @param data
 * @returns {*}
 */
export function createDictData(data) {
    return request({
        url: '/system/dict-data/create',
        method: 'post',
        data: data
    })
}

/**
 * 修改字典数据
 *
 * @param data
 * @returns {*}
 */
export function updateDictData(data) {
    return request({
        url: '/system/dict-data/update',
        method: 'put',
        data: data
    })
}

/**
 * 删除字典数据
 *
 * @param dictCode
 * @returns {*}
 */
export function deleteDictData(dictCode) {
    return request({
        url: '/system/dict-data/delete?id=' + dictCode,
        method: 'delete'
    })
}
