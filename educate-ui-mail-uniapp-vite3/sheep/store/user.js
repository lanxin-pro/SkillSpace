import {
    defineStore
} from 'pinia';
import cart from './cart';
import { clone, cloneDeep } from "lodash"
import UserApi from '@/sheep/api/user/index.js'

// 默认用户信息
const defaultUserInfo = {
    avatar: '', // 头像
    nickname: '', // 昵称
    gender: 0, // 性别
    mobile: '', // 手机号
    money: '--', // 余额
    commission: '--', // 佣金
    score: '--', // 积分
    verification: {}, // 认证字段
};

// 默认订单、优惠券等其他资产信息
const defaultNumData = {
    coupons_num: '--',
    order_num: {
        aftersale: 0,
        nocomment: 0,
        noget: 0,
        nosend: 0,
        unpaid: 0,
    },
};

const user = defineStore({
    id: 'user',
    state: () => ({
        userInfo: clone(defaultUserInfo), // 用户信息
        isLogin: !!uni.getStorageSync('token'), // 登录状态
        lastUpdateTime: 0, // 上次更新时间
    }),

    actions: {
        // 获取个人信息
        // TODO 芋艿：整理下；
        async getInfo() {
            const {
                code,
                data
            } = await UserApi.profile();
            console.log('用户的数据', data)
            this.userInfo = data;

            return Promise.resolve(data);
        },

        async updateUserData() {
            if (!this.isLogin) {
                this.resetUserData();
                return;
            }
            const nowTime = new Date().getTime();
            if (this.lastUpdateTime + 5000 > nowTime) return;
            await this.getInfo();
            // this.getNumData();
            this.lastUpdateTime = nowTime;
            return this.userInfo;
        },

        // 重置用户默认数据
        // TODO j-sentinel：整理下；
        resetUserData() {
            this.setToken();
            this.userInfo = clone(defaultUserInfo);
            this.numData = cloneDeep(defaultNumData);
            this.agentInfo = {};
            cart().emptyList();
        },

        // 设置token
        // TODO 芋艿：整理下；
        setToken(token = '') {
            if (token === '') {
                this.isLogin = false;
                uni.removeStorageSync('token');
            } else {
                this.isLogin = true;
                uni.setStorageSync('token', token);
                this.loginAfter();
            }
            return this.isLogin;
        },
        // 登录后
        // TODO 芋艿：整理下；
        async loginAfter() {
            // 登录后的示例
            console.log('loginAfter方法开始加载用户信息')
            await this.updateUserData();
            cart().getList();
        }
    },
    persist: {
        enabled: true,
        /* 目前不想复刻出BUG了，推测这里的key不一样 */
        /* strategies: [{
            key: 'user-store',
        }, ],*/
    },
});

export default user;
