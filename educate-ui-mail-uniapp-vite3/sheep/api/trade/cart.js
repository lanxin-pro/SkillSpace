import request from "@/utils/request.js"

/**
 * 获取购物车的数量
 *
 * @returns {*}
 */
export function getCartCount() {
    return request.get("app-api/trade/cart/get-count")
}
