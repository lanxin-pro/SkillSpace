import request from "@/utils/request"

/**
 * 查询参数列表
 * @returns {Promise<*>}
 */
export async function getConfigPage(params) {
    return request({
        url: '/infra/config/page',
        method: 'get',
        params: params
    })
}

/**
 * 查询参数详情
 *
 * @param id
 * @returns {Promise<*>}
 */
export async function getConfig(id) {
    return request({
        url: '/infra/config/get?id=' + id,
        method: 'get'
    })
}

/**
 * 根据参数键名查询参数值
 *
 * @param configKey
 * @returns {Promise<*>}
 */
export async function getConfigKey(configKey) {
    return request({
        url: '/infra/config/get-value-by-key?key=' + configKey,
        method: 'get'
    })
}

/**
 * 新增参数
 *
 * @param data
 * @returns {Promise<*>}
 */
export async function createConfig(data) {
    return request({
        url: '/infra/config/create',
        method: 'post',
        data: data
    })
}

/**
 * 修改参数
 *
 * @param data
 * @returns {Promise<*>}
 */
export async function updateConfig(data) {
    return request({
        url: '/infra/config/update',
        method: 'put',
        data: data
    })
}

/**
 * 删除参数
 *
 * @param id
 * @returns {Promise<*>}
 */
export async function deleteConfig(id) {
    return request({
        url: '/infra/config/delete?id=' + id,
        method: 'delete'
    })
}

/**
 * 导出参数
 *
 * @param query
 * @returns {Promise<*>}
 */
export async function exportConfig(query) {
    return request({
        url: '/infra/config/export',
        method: 'get',
        params: query,
        responseType: 'blob'
    })
}
