import request from "../../../sheep/request2"

/**
 * 获得商品 SPU 分页
 *
 * @param data
 * @returns {*}
 */
export function getSpuPage(data) {
    return request({
        url: 'product/spu/page',
        method: 'GET',
        params: data
    })
}
