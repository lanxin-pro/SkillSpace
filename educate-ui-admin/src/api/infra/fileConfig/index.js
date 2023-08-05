import request from "@/utils/request.js";

/**
 * 查询文件配置列表
 *
 * @param params
 * @returns {*}
 */
export function getFileConfigPage(params) {
    return request({
        url: '/infra/file-config/page',
        method: 'get',
        params: params
    })
}

/**
 * 新增文件配置
 *
 * @param data
 * @returns {*}
 */
export function createFileConfig(data) {
    return request({
        url: '/infra/file-config/create',
        method: 'post',
        data: data
    })
}

/**
 * 修改文件配置
 *
 * @param data
 * @returns {*}
 */
export function updateFileConfig(data) {
    return request({
        url: '/infra/file-config/update',
        method: 'put',
        data: data
    })
}

/**
 * 删除文件配置
 *
 * @param id
 * @returns {*}
 */
export function deleteFileConfig(id) {
    return request({
        url: '/infra/file-config/delete?id=' + id,
        method: 'delete'
    })
}

/**
 * 测试文件配置
 *
 * @param id
 * @returns {*}
 */
export function testFileConfig(id) {
    return request({
        url: '/infra/file-config/test?id=' + id,
        method: 'get'
    })
}

/**
 * 更新文件配置为主配置
 *
 * @param id
 * @returns {*}
 */
export function updateFileConfigMaster(id) {
    return request({
        url: '/infra/file-config/update-master?id=' + id,
        method: 'put'
    })
}

/**
 * 查询文件配置详情
 *
 * @param id
 * @returns {*}
 */
export function getFileConfig(id) {
    return request({
        url: '/infra/file-config/get?id=' + id,
        method: 'get'
    })
}
