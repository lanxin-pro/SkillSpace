import request from '@/utils/request'

/**
 * 获取验证图片以及 token
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export async function getCode(data){
    return await request({
        url: "/system/captcha/get",
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
        url: "/system/captcha/check",
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

export function logout(){
    return request({
        url: '/system/auth/logout',
        method: 'post'
    })
}

/**
 * 获取登录验证码
 * @param mobile
 * @param scene
 * @returns {*}
 */
export function sendSmsCode(mobile,scene){
    const data = {
        mobile,
        scene
    }
    return request({
        url: '/system/auth/send-sms-code',
        method: 'post',
        data: data
    })
}

/**
 * 短信验证码登录
 * @param mobile
 * @param scene
 * @returns {*}
 */
export function smsLogin(data){
    return request({
        url: '/system/auth/sms-login',
        method: 'post',
        data: data
    })
}

/**
 * 使用租户名，获得租户编号
 * @param name
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getTenantIdByName(name) {
    return request({
        url: '/system/tenant/get-id-by-name',
        method: 'get',
        params: {
            name
        }
    })
}

/**
 * 社交授权的跳转
 * @param type
 * @param redirectUri
 * @returns {*}
 */
export function socialAuthRedirect(type,redirectUri){
    return request({
        url: '/system/auth/social-auth-redirect?type=' + type + '&redirectUri=' + redirectUri,
        method: 'get',
    })
}

/**
 * 社交快捷登录，使用 code 授权码
 * @param type
 * @param code
 * @param state
 * @returns {*}
 */
export function socialLogin(type, code, state) {
    return request({
        url: '/system/auth/social-login',
        method: 'post',
        data: {
            type,
            code,
            state
        }
    })
}

/**
 * 获取用户详细信息
 *
 * @returns {*}
 */
export function getInfo() {
    return request({
        url: '/system/auth/get-permission-info',
        method: 'get'
    })
}
