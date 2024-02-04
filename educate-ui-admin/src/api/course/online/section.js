import request from '@/utils/request'


export function findSectionList(courseId) {
    if(!courseId) {
        return
    }
    return request({
        url: '/course/online/section/list?courseId=' + courseId,
        method: 'get'
    })
}


/**
 * 保存和修改章节管理
 *
 * @param data
 * @returns {*}
 */
export function saveUpdateChapterLesson(data) {
    return request({
        url: '/course/online/section/saveupdate',
        method: 'post',
        data: data
    })
}

/**
 * 保存和修改章节管理
 *
 * @param data
 * @returns {*}
 */
export function getChapterLessons(opid) {
    return request({
        url: '/course/online/section/get?opid=' + opid,
        method: 'get'
    })
}
