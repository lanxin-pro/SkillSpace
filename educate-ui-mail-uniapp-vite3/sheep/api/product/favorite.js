import request from "@/utils/request.js"

/**
 * 检查是否收藏过商品
 *
 * @param spuId
 * @returns {*}
 */
export function isFavoriteExists(spuId) {
    return request.get('app-api/product/favorite/exits', {
        spuId
    });
}

/**
 * 取消商品收藏
 *
 * @param spuId
 * @returns {*}
 */
export function deleteFavorite(spuId) {
    return request.delete('app-api/product/favorite/delete', {
        spuId
    });
}

/**
 * 添加商品收藏
 *
 * @param spuId
 * @returns {*}
 */
export function createFavorite(spuId) {
    return request.post('app-api/product/favorite/create', {
        spuId
    });
}
