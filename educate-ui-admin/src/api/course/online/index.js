import request from '@/utils/request'

/**
 * 获得课程分页
 *
 * @param query 分页条件
 * @returns {*}
 */
export function getCourseOnlineInfo(query) {
    return request({
        url: '/course/online/page',
        method: 'get',
        params: query
    })
}

/**
 * 获取课程信息详情
 *
 * @param courseId
 * @returns {*}
 */
export function getCourseOnline(courseId) {
    return request({
        url: '/course/online/get?id=' + courseId,
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
        url: '/course/online/create',
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
        url: '/course/online/update',
        method: 'put',
        data: data
    })
}
