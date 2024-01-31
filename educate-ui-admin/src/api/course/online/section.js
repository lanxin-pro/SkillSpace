import request from '@/utils/request'

/**
 * 获得课程分页
 *
 * @param query 分页条件
 * @returns {*}
 */
export function getCourseOnlineInfo(query) {
    return request({
        url: '/course/online/chapter/page',
        method: 'get',
        params: query
    })
}

export function findChapterList(courseId) {
    if(!courseId) {
        return
    }
    return request({
        url: '/course/online/section/list?courseId=' + courseId,
        method: 'get'
    })
}

/**
 * 获取课程信息详情
 *
 * @param courseId
 * @returns {*}
 */
export function getCourseOnlineId(courseId) {
    return request({
        url: '/course/online/section/list',
        method: 'get'
    })
}

/**
 * 创建课程详情
 *
 * @param data
 * @returns {*}
 */
export function createCourseOnline(data) {
    return request({
        url: '/course/online/chapter/create',
        method: 'post',
        data: data
    })
}

/**
 * 修改课程详细
 *
 * @param data
 * @returns {*}
 */
export function updateCourseOnline(data) {
    return request({
        url: '/course/online/chapter/update',
        method: 'put',
        data: data
    })
}

/**
 * 修改课程的状态
 *
 * @param data
 * @returns {*}
 */
export function updateStatusCourseOnline(data) {
    return request({
        url: '/course/online/chapter/updateStatus',
        method: 'put',
        data: data
    })
}

/**
 * 批量删除课程的章
 *
 * @param ids 课程的ids
 * @returns {*}
 */
export function courseDeleteBatchIds(ids) {
    if(!ids){
        return
    }
    return request({
        url: '/course/online/chapter/delBatch?ids=' + ids,
        method: 'delete'
    })
}

/**
 * 删除课程的章
 *
 * @param id 课程id
 * @returns {*}
 */
export function courseDelete(id) {
    return request({
        url: '/course/online/chapter/delete?id=' + id,
        method: 'delete'
    })
}


