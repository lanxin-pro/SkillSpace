import { Debounce } from '@/utils/validate.js'
import store from "../store"
import { LOGIN_STATUS, USER_INFO, EXPIRES_TIME, STATE_R_KEY, BACK_URL } from '@/config/cache.js'
import Cache from '@/utils/cache.js'

// 防抖函数
export const toLogin = Debounce(_toLogin, 300)

function prePage(){
    let pages = getCurrentPages();
    let prePage = pages[pages.length - 1];
    return prePage.route;
}

/** 通用登录函数 */
export function _toLogin(push, pathLogin) {
    console.log('登出')
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

export function checkLogin() {
    let token = Cache.get(LOGIN_STATUS);
    let expiresTime = Cache.get(EXPIRES_TIME);
    let newTime = Math.round(new Date() / 1000);
    if (expiresTime < newTime || !token){
        Cache.clear(LOGIN_STATUS);
        Cache.clear(EXPIRES_TIME);
        Cache.clear(USER_INFO);
        Cache.clear(STATE_R_KEY);
        return false;
    }else {
        console.log('开始刷新令牌')
        store.commit('UPDATE_LOGIN', token);
        let userInfo = Cache.get(USER_INFO,true);
        if(userInfo){
            store.commit('UPDATE_USERINFO', userInfo);
        }
        return true;
    }

}
