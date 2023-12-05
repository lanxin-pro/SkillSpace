/* 导入的变量包含API请求的基础URL、默认请求头、认证token的名称和特定的请求头参数 */
import {
    HTTP_REQUEST_URL,
    HEADER,
    TOKENNAME,
    HEADERPARAMS
} from '@/config/app'
/*  */
import {
    toLogin,
    checkLogin
} from '../libs/login.js'
import store from '../store'
import { getTerminal } from "./util.js"

// TODO j-sentinel：临时解决 uniapp 在小程序，undefined 会被 tostring 的问题
/* 这个函数用于递归地遍历一个对象，删除所有未定义(undefined)的属性。这对于清理请求数据很有用，因为未定义的属性可能导致问题 */
function deleteUndefinedProperties(obj) {
    for (let key in obj) {
        if (obj.hasOwnProperty(key)) {
            if (typeof obj[key] === 'object' && obj[key] !== null) {
                deleteUndefinedProperties(obj[key]); // 递归调用，处理嵌套的对象
            } else if (obj[key] === undefined) {
                delete obj[key];
            }
        }
    }
}

/**
 * 发送请求
 *
 * 这是一个用于发送网络请求的核心函数
 * 参数包括URL、请求方法、数据、认证和校验选项以及额外的参数
 * 它首先检查和处理认证：如果用户未登录且需要认证，则调用 toLogin 函数
 * 然后删除请求数据中的未定义属性
 * 根据URL的不同部分调整请求头，例如添加租户ID或设备信息。
 * 如果用户已经登录且有token，将token添加到请求头。
 * 使用 uni.request 发送请求，这是uni-app框架提供的网络请求方法。
 * 根据返回的数据执行resolve或reject操作。
 */
function baseRequest(url, method, data, {
    noAuth = false,
    noVerify = false
}, params) {
    let Url = HTTP_REQUEST_URL,header = HEADER
    if (params != undefined) {
        header = HEADERPARAMS;
    }
    if (!noAuth) {
        // 登录过期自动登录
        if (!store.state.app.token && !checkLogin()) {
            // debugger
            toLogin();
            return Promise.reject({
                msg: '未登录'
            });
        }
    }

    deleteUndefinedProperties(data)

    // TODO j-sentinel 补个 header 多租户
    if (url.indexOf('app-api') >= 0) {
        header = {
            ...header
        }
        header['tenant-id'] = 1
    }

    // 终端
    header['terminal'] = getTerminal()

    if (store.state.app.token) {
        // header[TOKENNAME] = store.state.app.token;
        header['Authorization'] = 'Bearer ' + store.state.app.token;
    }
    return new Promise((reslove, reject) => {
        uni.request({
            url: 'http://127.0.0.1:9011/server/' + url,
            method: method || 'GET',
            header: header,
            data: data || {},
            success: (res) => {
                console.log('请求返回的值', res)
                console.log(noVerify)
                if (noVerify) {
                    reslove(res.data, res);
                } else if (res.data.code === 200 || res.data.code === 0) {
                    reslove(res.data, res);
                } else if ([410000, 410001, 410002, 401].indexOf(res.data.code) !== -1) {
                    toLogin();
                    reject(res.data);
                } else {
                    reject(res.data.msg || res.data.message || '系统错误');
                }
            },
            fail: (msg) => {
                reject('请求失败', msg);
            }
        })
    });
}


const request = {};

/* 首次会加载这个方法 */
['options', 'get', 'post', 'put', 'head', 'delete', 'trace', 'connect'].forEach((method) => {
    request[method] = (api, data, opt, params) => baseRequest(api, method, data, opt || {}, params)
});

export default request;
