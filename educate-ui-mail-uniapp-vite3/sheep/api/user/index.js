import request2 from '@/sheep/request2'

export default {

    /**
     * 获取用户的信息
     */
    profile: () => {
        return request2({
            url: 'member/user/get',
            method: 'GET',
            custom: {
                showLoading: false,
                auth: true,
            },
        })
    },

    /**
     * 支付
     * @returns {*}
     */
    balance: () => {
        return request2({
            url: 'pay/wallet/get',
            method: 'GET',
            custom: {
                showLoading: false,
                auth: true,
            },
        })
    },


}

