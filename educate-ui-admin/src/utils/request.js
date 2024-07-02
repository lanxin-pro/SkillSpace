import axios from 'axios'
import errorCode from '@/utils/errorCode'
import { ElMessage, ElMessageBox, ElNotification } from 'element-plus'
import { getAccessToken, getRefreshToken,setToken } from "@/utils/auth.js"
import { getPath } from "@/utils/ruoyi.js"
import { useUserStore } from '@/piniastore/modules/user.js'
import { refreshToken } from '@/api/login/index.js'
import qs from 'qs'

// 需要忽略的提示。忽略后，自动 Promise.reject('error')
const ignoreMsgs = [
    "无效的刷新令牌", // 刷新令牌被删除时，不用提示
    "刷新令牌已过期" // 使用刷新令牌，刷新获取新的访问令牌时，结果因为过期失败，此时需要忽略。否则，会导致继续 401，无法跳转到登出界面
]

// 是否显示重新登录
export let isRelogin = { show: false }
// Axios 无感知刷新令牌，参考 https://www.dashingdog.cn/article/11 与 https://segmentfault.com/a/1190000020210980 实现
// 请求队列
let requestList = []
// 是否正在刷新中
let isRefreshToken = false

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'


const request = axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: import.meta.env.VITE_BASE_API + "/server/admin-api", // 此处的 /admin-api/ 地址，原因是后端的基础路径为 /admin-api/
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

    if (
        config.method?.toUpperCase() === 'POST' &&
        (config.headers)['Content-Type'] === 'application/x-www-form-urlencoded'
    ){
        alert("开始执行qs.stringify(data)")
        config.data = qs.stringify(data)
    }

    // get请求映射params参数
    if (config.method === 'get' && config.params) {
        let url = config.url + '?'
        for (const propName of Object.keys(config.params)) {
            const value = config.params[propName]
            const part = encodeURIComponent(propName) + '='
            if (value !== null && typeof(value) !== "undefined") {
                if (typeof value === 'object') {
                    for (const key of Object.keys(value)) {
                        let params = propName + '[' + key + ']'
                        const subPart = encodeURIComponent(params) + '='
                        url += subPart + encodeURIComponent(value[key]) + "&"
                    }
                } else {
                    url += part + encodeURIComponent(value) + "&"
                }
            }
        }
        // 给 get 请求加上时间戳参数，避免从缓存中拿数据
        // const now = new Date().getTime()
        // params = params.substring(0, url.length - 1) + `?_t=${now}`
        url = url.slice(0, -1);
        config.params = {};
        config.url = url;
    }
    return config
},error => {
    alert(error)
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
        // 如果未认证，并且未进行刷新令牌，说明可能是访问令牌过期了
        if(!isRefreshToken){
            isRefreshToken = true
            //  如果获取不到刷新令牌，则只能执行登出操作
            if(!getRefreshToken()){
                return handleAuthorized()
            }
            // 进行访问令牌的刷新
            try{
                const refreshTokenResponse = await refreshToken()
                setToken(refreshTokenResponse.data)
                return request(res.config)
            }catch (error){// 为什么需要 catch 异常呢？刷新失败时，请求因为 Promise.reject 触发异常。
                // 提示是否要登出。即不回放当前请求！不然会形成递归
                return handleAuthorized()
            }finally {
                isRefreshToken = false
            }
        }
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

const handleAuthorized = ()=>{
    const store = useUserStore()

    if(!isRelogin.show){
        isRelogin.show = true
        ElMessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
                confirmButtonText: '重新登录',
                cancelButtonText: '取消',
                type: 'warning'
            }
        ).then(() => {
            isRelogin.show = false
            store.loginOut().then(() => {
                location.href = getPath('/index')
            })
        }).catch(() => {
            isRelogin.show = false
        })
    }
    return Promise.reject('无效的会话，或者会话已过期，请重新登录。')

}


/*导出异步请求 */
export default request;
