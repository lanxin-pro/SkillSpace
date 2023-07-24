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
        params: data
    })
}
