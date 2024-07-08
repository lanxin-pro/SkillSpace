import request from '@/utils/request'

/**
 * 获得【我的】聊天对话
  * @param courseId
 * @returns {*}
 */
export function getChatConversationMy(id) {
    if(!id) {
        return
    }
    return request({
        url: `/ai/chat/conversation/get-my?id=${id}`,
        method: 'get'
    })
}

/**
 * 获得【我的】聊天对话列表
 * @returns {*}
 */
export function getChatConversationMyList() {
    return request({
        url: `/ai/chat/conversation/my-list`,
        method: 'get'
    })
}
