import { defineStore } from 'pinia'
import CartApi from '@/sheep/api/trade/cart'

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
        // 获取购物车列表
        async getList() {
            const { data, code } = await CartApi.getCartList();
            if (code === 0) {
                this.list = data.validList

                // 计算各种关联属性
                this.selectedIds = []
                this.isAllSelected = true
                this.totalPriceSelected = 0
                this.list.forEach((item) => {
                    if (item.selected) {
                        this.selectedIds.push(item.id);
                        this.totalPriceSelected += item.count * item.sku.price
                    } else {
                        this.isAllSelected = false
                    }
                });
            }
        },
        // 添加购物车
        async add(goodsInfo) {
            // 添加购物项
            const { code } = await CartApi.addCart({
                skuId: goodsInfo.id,
                count: goodsInfo.goods_num,
            });
            // 刷新购物车列表
            if (code === 0) {
                await this.getList();
            }
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
