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
                // 有效的购物项数组
                this.list = data.validList

                // 计算各种关联属性
                this.selectedIds = []
                this.isAllSelected = true
                this.totalPriceSelected = 0
                console.log('totalPriceSelected总价', this.totalPriceSelected)
                this.list.forEach((item) => {
                    if (item.selected) {
                        this.selectedIds.push(item.id);
                        console.log('item.count商品总数', item.count)
                        console.log('item.sku.price单个商品价格', item.sku.price)
                        this.totalPriceSelected += item.count * item.sku.price
                    } else {
                        // 无效的购物项数组
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
            console.log('添加购物车的方法', code)
            // 刷新购物车列表
            if (code === 0) {
                await this.getList();
            }
        },

        // 清空购物车
        async emptyList() {
            alert('清空购物车')
            await this.delete(this.list.map((item) => item.id));
        },

        // 单选购物车商品
        async selectSingle(goodsId) {
            const { code } = await CartApi.updateCartSelected({
                ids: [goodsId],
                selected: !this.selectedIds.includes(goodsId), // selectedIds选中的列表 取反
            });
            if (code === 0) {
                await this.getList()
            }
        },
    },
    persist: {
        enabled: true,
        /* 目前不想复刻出BUG了，推测这里的key不一样 */
        /* strategies: [
            {
                key: 'cart-store',
            },
        ],*/
    },
});

export default cart;
