import request2 from "@/sheep/request2/index.js"

const AuthApi = {
    // 使用手机 + 验证码登录
    smsLogin: (data) => {
        return request2({
            url: 'member/auth/sms-login',
            method: 'POST',
            data,
            custom: {
                showSuccess: true,
                loadingMsg: '登录中',
                successMsg: '登录成功',
            },
        });
    },
    // 发送手机验证码
    sendSmsCode: (mobile, scene) => {
        return request2({
            url: 'member/auth/send-sms-code',
            method: 'POST',
            data: {
                mobile,
                scene,
            },
            custom: {
                loadingMsg: '发送中',
                showSuccess: true,
                successMsg: '发送成功',
            },
        });
    },
}

export default AuthApi
