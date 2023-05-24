import axios from 'axios'
import errorCode from '@/utils/errorCode'
import { ElMessage, ElMessageBox, ElNotification } from 'element-plus'
import {getAccessToken} from "@/utils/auth.js";

// 需要忽略的提示。忽略后，自动 Promise.reject('error')
const ignoreMsgs = [
    "无效的刷新令牌", // 刷新令牌被删除时，不用提示
    "刷新令牌已过期" // 使用刷新令牌，刷新获取新的访问令牌时，结果因为过期失败，此时需要忽略。否则，会导致继续 401，无法跳转到登出界面
]



axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'


const request = axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: import.meta.env.VITE_APP_BASE_API + "/server/admin-api", // 此处的 /admin-api/ 地址，原因是后端的基础路径为 /admin-api/
    // 指定请求超时的毫秒数(0 表示无超时时间)
    timeout: 30000,
    // 禁用 Cookie 等信息
    withCredentials: false,
});

// 请求拦截器
request.interceptors.request.use(config => {
    // 是否需要设置 token
    const isToken = (config.headers || {}).isToken === false
    if(getAccessToken() && !isToken){
        config.headers['Authorization'] = 'Bearer ' + getAccessToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    return config
},error => {
    console.log(error)
    Promise.reject(error)
})

// 响应拦截器
request.interceptors.response.use(async res => {
    // 未设置状态码则默认成功状态
    const code = res.data.code || 200
    // 获取错误信息
    const msg = res.data.msg || errorCode[code] || errorCode['default']
    if (ignoreMsgs.indexOf(msg) !== -1) { // 如果是忽略的错误码，直接返回 msg 异常
        return Promise.reject(msg)
    }else if(code === 401){

    }else if(code === 500){
        ElMessage({
            message: msg,
            type: 'error'
        })
        return Promise.reject(new Error(msg))
    }else if(code === 501){
        ElMessage({
            type: 'error',
            duration: 0,
            message: msg
        })
        return Promise.reject(new Error(msg))
    }else if(code === 901){

    }else if(code !== 200){
        if (msg === '无效的刷新令牌') { // hard coding：忽略这个提示，直接登出
            console.log(msg)
        } else {
            ElNotification.error({
                title: "异常：" + msg
            })
        }
        return Promise.reject('error')
    }else{
        return res.data
    }
},error => {
    console.log('err',error)
    let { message } = error
    if (message === "Network Error") {
        message = "后端接口连接异常";
    } else if (message.includes("timeout")) {
        message = "系统接口请求超时";
    } else if (message.includes("Request failed with status code")) {
        message = "系统接口" + message.substr(message.length - 3) + "异常";
    }
    ElMessage({
        message: message,
        type: 'error',
        duration: 5 * 1000
    })
    return Promise.reject(error)
})

/*导出异步请求 */
export default request;
