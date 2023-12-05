import request from "@/utils/request.js"

/**
 * 发送手机验证码
 *
 * @param mobile
 * @param scene
 * @returns {*}
 */
export function sendSmsCode(mobile, scene) {
    return request.post('app-api/member/auth/send-sms-code', {
        mobile,
        scene
    }, {
        // TODO j-sentinel：后续要做调整
        noAuth: true
    });
}

/**
 * 使用手机 + 验证码登录
 *
 * @param data
 * @returns {*}
 */
export function smsLogin(data) {
    return request.post('app-api/member/auth/sms-login', data, {
        // TODO j-sentinel：后续要做调整
        noAuth: true
    });
}
