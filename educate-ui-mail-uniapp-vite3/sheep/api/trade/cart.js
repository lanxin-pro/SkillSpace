import request2 from "@/sheep/request2/index.js"

const CartApi = {
    /**
     * 添加到购物车中
     * @param data
     * @returns {*}
     */
    addCart: (data) => {
        return request2({
            url: 'trade/cart/add',
            method: 'POST',
            data: data,
            // TODO j-sentinel：这里没提示
            custom: {
                showSuccess: true,
                successMsg: '已添加到购物车~',
            }
        });
    },
    getCartList: () => {
        return request2({
            url: 'trade/cart/list',
            method: 'GET',
        });
    },

    updateCartSelected: (data) => {
        return request2({
            url: 'trade/cart/update-selected',
            method: 'PUT',
            data: data
        });
    },

}

export default CartApi;
