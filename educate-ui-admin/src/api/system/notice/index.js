import request from '@/utils/request'

/**
 * 查询公告列表
 *
 * @param query
 * @returns {*}
 */
export const getNoticePage = (params) => {
    return request({
        url: '/system/notice/page',
        method: 'get',
        params: params
    })
}

/**
 * 查询公告详情
 *
 * @param id
 * @returns {*}
 */
export const getNotice = (id) => {
    return request({
        url: '/system/notice/get?id=' + id,
        method: 'get'
    })
}

/**
 * 新增公告
 *
 * @param data
 * @returns {*}
 */
export const createNotice = (data) => {
    return request({
        url: '/system/notice/create',
        method: 'post',
        data: data
    })
}

/**
 * 修改公告
 *
 * @param data
 * @returns {*}
 */
export const updateNotice = (data) => {
    return request({
        url: '/system/notice/update',
        method: 'put',
        data: data
    })
}

/**
 * 删除公告
 *
 * @param id
 * @returns {*}
 */
export const deleteNotice = (id) => {
    return request({
        url: '/system/notice/delete?id=' + id,
        method: 'delete'
    })
}
