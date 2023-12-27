import request from "../../../sheep/request2"

/**
 * 获得商品 SPU 分页
 *
 * @param data
 * @returns {*}
 */
export function getSpuPage(params) {
    return request({
        url: 'product/spu/page',
        method: 'GET',
        params
    })
}

/**
 * 查询商品
 *
 * @param data
 * @returns {*}
 */
export function getSpuDetail(id, params = {}) {
    return request({
        url: 'product/spu/get-detail?id=' + id,
        method: 'GET',
        params,
        custom: {
            showLoading: false,
            showError: false,
        },
    })
}
