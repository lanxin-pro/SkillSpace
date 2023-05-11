import request from '@/utils/request'

/**
 * 获取验证图片以及 token
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export async function getCode(data){
    return await request({
        url: "system/captcha/get",
        method: 'post',
        data
    })
}

/**
 * 滑动或者点选验证
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export async function reqCheck(data){
    return await request({
        url: "system/captcha/check",
        method: 'post',
        data
    })
}

/**
 * 登录
 * @param username
 * @param password
 * @param captchaVerification
 * @param socialType
 * @param socialCode
 * @param socialState
 * @returns {*}
 */
export function login(username, password, captchaVerification, socialType, socialCode, socialState){
    const data = {
        username,
        password,
        captchaVerification,
        // 社交相关
        socialType,
        socialCode,
        socialState
    }
    return request({
        url: '/system/auth/login',
        method: 'post',
        data: data
    })
}
