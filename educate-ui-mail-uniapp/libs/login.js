import { Debounce } from '@/utils/validate.js'
import store from "../store"
import { LOGIN_STATUS, USER_INFO, EXPIRES_TIME, STATE_R_KEY, BACK_URL } from '@/config/cache.js'
import Cache from '@/utils/cache.js'

// 防抖函数
export const toLogin = Debounce(_toLogin,300)

function prePage(){
    let pages = getCurrentPages();
    let prePage = pages[pages.length - 1];
    return prePage.route;
}

/** 通用登录函数 */
export function _toLogin(push, pathLogin) {
    let login_back_url = Cache.get(BACK_URL)
    let path = prePage()
    store.commit("LOGOUT")
    // path = location.href;
    path = location.pathname + location.search
    if(!pathLogin){
        // 想要跳转的路径
        pathLogin = '/page/users/login/index'
        // set backUrl 的值
        Cache.set('login_back_url', path)
    }
    // 如果不是从这个页面来的，就直接跳转登录页面 - 通用登录函数
    if (['/pages/user/index'].indexOf(login_back_url) === -1) {
        uni.navigateTo({
            url: '/pages/users/login/index'
        })
    }
}
