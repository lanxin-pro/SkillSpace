import request from '@/utils/request'
import qs from 'qs'

/**
 * 创建敏感词
 *
 * @param data
 * @returns {*}
 */
export function createSensitiveWord(data) {
    return request({
        url: '/system/sensitive-word/create',
        method: 'post',
        data: data
    })
}

/**
 * 更新敏感词
 *
 * @param data
 * @returns {*}
 */
export function updateSensitiveWord(data) {
    return request({
        url: '/system/sensitive-word/update',
        method: 'put',
        data: data
    })
}

/**
 * 删除敏感词
 *
 * @param id
 * @returns {*}
 */
export function deleteSensitiveWord(id) {
    return request({
        url: '/system/sensitive-word/delete?id=' + id,
        method: 'delete'
    })
}

/**
 * 获得敏感词
 *
 * @param id
 * @returns {*}
 */
export function getSensitiveWord(id) {
    return request({
        url: '/system/sensitive-word/get?id=' + id,
        method: 'get'
    })
}

/**
 * 获得敏感词分页
 *
 * @param query
 * @returns {*}
 */
export function getSensitiveWordPage(query) {
    return request({
        url: '/system/sensitive-word/page',
        method: 'get',
        params: query
    })
}

/**
 * 导出敏感词 Excel
 *
 * @param query
 * @returns {*}
 */
export function exportSensitiveWordExcel(query) {
    return request({
        url: '/system/sensitive-word/export-excel',
        method: 'get',
        params: query,
        responseType: 'blob'
    })
}

/**
 * 获取所有敏感词的标签数组
 *
 * @returns {*}
 */
export function getSensitiveWordTagList(){
    return request({
        url: '/system/sensitive-word/get-tags',
        method: 'get'
    })
}

/**
 * 获得文本所包含的不合法的敏感词数组
 *
 * @param query
 * @returns {*}
 */
export function validateText(query) {
    return request({
        url: '/system/sensitive-word/validate-text?' + qs.stringify(query, {arrayFormat: 'repeat'}),
        method: 'get',
    })
}
