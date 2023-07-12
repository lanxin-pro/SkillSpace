import request from '@/utils/request'

/**
 * 用户头像上传
 * @param data
 * @returns {*}
 */
export const uploadAvatar = (data) => {
    return request({
        url: '/system/user/profile/update-avatar',
        method: 'post',
        data: data,
    })
}
