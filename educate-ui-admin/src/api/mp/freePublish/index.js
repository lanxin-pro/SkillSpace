import request from '@/utils/request'

/**
 * 获得公众号素材分页
 *
 * @param query
 * @returns {*}
 */
export function getFreePublishPage(query) {
    return request({
        url: '/mp/free-publish/page',
        method: 'get',
        params: query
    })
}

/**
 * 删除公众号素材
 *
 * @param accountId
 * @param articleId
 * @returns {*}
 */
export function deleteFreePublish(accountId, articleId) {
    return request({
        url: '/mp/free-publish/delete?accountId=' + accountId + '&articleId=' + articleId,
        method: 'delete'
    })
}

/**
 * 发布公众号素材
 *
 * @param accountId
 * @param mediaId
 * @returns {*}
 */
export function submitFreePublish(accountId, mediaId) {
    return request({
        url: '/mp/free-publish/submit?accountId=' + accountId + '&mediaId=' + mediaId,
        method: 'post'
    })
}
