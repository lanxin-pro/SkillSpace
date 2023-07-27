import request from '@/utils/request'

/**
 * 获得表定义分页
 *
 * @param query
 * @returns {*}
 */
export function getCodegenTablePage(query) {
    return request({
        url: '/infra/codegen/table/page',
        method: 'get',
        params: query
    })
}


/**
 * 获得表定义分页
 *
 * @param query
 * @returns {*}
 */
export function getSchemaTableList(query) {
    return request({
        url: '/infra/codegen/db/table/list',
        method: 'get',
        params: query
    })
}

/**
 * 基于数据库的表结构，创建代码生成器的表定义
 *
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export const createCodegenList = (data) => {
    return request({
        url: '/infra/codegen/create-list',
        method: 'post',
        data: data
    })
}

/**
 * 删除代码生成表定义
 *
 * @param data
 * @returns {*}
 */
export function deleteCodegenTable(tableId) {
    return request({
        url: '/infra/codegen/delete?tableId=' + tableId,
        method: 'delete'
    })
}

/**
 * 查询详情代码生成表定义
 *
 * @param id
 * @returns {Promise<AxiosResponse<any>>}
 */
export const getCodegenTable = (id) => {
    return request({
        url: '/infra/codegen/detail?tableId=' + id,
        method: 'get',
    })
}

/**
 * 预览生成代码
 *
 * @param id
 * @returns {Promise<AxiosResponse<any>>}
 */
export const previewCodegen = (id) => {
    return request({
        url: '/infra/codegen/preview?tableId=' + id,
        method: 'get',
    })
}
