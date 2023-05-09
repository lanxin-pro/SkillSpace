import request from '@/utils/request'

// 获取验证图片以及 token
export async function getCode(data){
    return await request({
        url: "system/captcha/get",
        method: 'post',
        data
    })
}

// 滑动或者点选验证
export async function reqCheck(data){
    return await request({
        url: "system/captcha/check",
        method: 'post',
        data
    })
}

