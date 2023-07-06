import request from '@/utils/request'

/**
 * 获取部门精简信息列表
 *
 * @returns {*}
 */
export const listSimpleDepts = ()=> {
    return request({
        url: '/system/dept/list-all-simple',
        method: 'get'
    })
}
