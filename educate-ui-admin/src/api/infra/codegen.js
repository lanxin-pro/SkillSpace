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
