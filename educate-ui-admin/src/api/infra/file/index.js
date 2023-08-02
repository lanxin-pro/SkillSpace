import request from '@/utils/request.js'

/**
 * 查询文件列表
 *
 * @param params
 * @returns {*}
 */
export function getFilePage(params) {
    return request({
        url: '/infra/file/page',
        method: 'get',
        params: params
    })
}

/**
 * 删除文件
 *
 * @param id
 * @returns {*}
 */
export function deleteFile(id) {
    return request({
        url: '/infra/file/delete?id=' + id,
        method: 'delete'
    })
}
