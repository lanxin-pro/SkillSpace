import router from './router'
import store from './store'
import { ElMessage } from 'element-plus'
import {showFullLoading,hideFullLoading} from '@/utils/index.js'
import { getAccessToken } from '@/utils/auth'

// 增加三方登陆
const whiteList = ['/login', '/social-login',  '/auth-redirect', '/bind', '/register', '/oauthLogin/gitee']

router.beforeEach((to,from,next)=>{
    // 开启动画
    showFullLoading()
    // 获取token
    if(getAccessToken()){
        console.log("错误，不存在token")
    }else{
        // 没有token
        console.log(to.path)
        if (whiteList.indexOf(to.path) !== -1) {
            // 在免登录白名单，直接进入
            hideFullLoading()
            next()
        } else {
            const redirect = encodeURIComponent(to.fullPath) // 编码 URI，保证参数跳转回去后，可以继续带上
            ElMessage({
                message: "很抱歉请您先登录",
                type: 'error'
            })
            next(`/login?redirect=${redirect}`) // 否则全部重定向到登录页
            hideFullLoading()
        }
    }


    next()
})

router.afterEach(() => {
    hideFullLoading()
})
