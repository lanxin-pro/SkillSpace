import { defineStore } from 'pinia'

const cart = defineStore({
    // 注意id要保持不变
    id: 'cart',
    state: () => ({
        // 购物车列表
        list: [],
        // 已选列表
        selectedIds: [],
        // 是否全选
        isAllSelected: false,
        // 选中项总金额
        totalPriceSelected: 0,
    }),
    actions: {
        // 添加购物车
        async add(goodsInfo) {
           console.log('添加购物车', goodsInfo)
        },
    },
    persist: {
        enabled: true,
        strategies: [
            {
                key: 'cart-store',
            },
        ],
    },
});

export default cart;
