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

/**
 * 查询部门列表
 *
 * @param params
 * @returns {*}
 */
export const getDeptPage = (params)=> {
    return request({
        url: '/system/dept/list',
        method: 'get',
        params: params
    })
}

/**
 * 删除部门
 *
 * @param id
 * @returns {*}
 */
export const deleteDept = (id)=> {
    return request({
        url: '/system/dept/delete?id=' + id,
        method: 'delete'
    })
}

/**
 * 查询部门详情
 *
 * @param id
 * @returns {*}
 */
export const getDept = (id)=> {
    return request({
        url: '/system/dept/get?id=' + id,
        method: 'get'
    })
}

/**
 * 新增部门
 *
 * @param data
 * @returns {Promise<*>}
 */
export const createDept = async (data) => {
    return request({
        url: '/system/dept/create',
        method: 'post',
        data: data
    })
}

/**
 * 修改部门
 *
 * @param params
 * @returns {Promise<*>}
 */
export const updateDept = async (params) => {
    return request({
        url: '/system/dept/update',
        method: 'put',
        data: params
    })
}
