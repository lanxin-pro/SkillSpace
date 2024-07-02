import {
    LOGIN_STATUS,
    UID,
    OPENID,
    PLATFORM,
    USER_INFO
} from '../../config/cache';
import Cache from '@/utils/cache.js'

const state = {
    token: Cache.get(LOGIN_STATUS) || ''
};

const mutations = {
    /** accessToken */
    LOGIN(state, opt) {
        state.token = opt.token;
        Cache.set(LOGIN_STATUS, opt.token);
    },
    /** 登出 */
    LOGOUT(state) {
        console.log('store内部，正在处理登出逻辑')
        state.token = undefined;
        state.uid = undefined
        Cache.clear(LOGIN_STATUS);
        Cache.clear(UID);
        Cache.clear(USER_INFO);
    },
};

const actions = {

};

export default {
    state,
    mutations,
    actions
};
