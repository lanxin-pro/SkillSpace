import request from "@/utils/request.js"

/**
 * 获得商品 SPU 列表
 *
 * @param recommendType
 * @returns {*}
 */
export function getSpuList(recommendType) {
    return request.get('app-api/product/spu/list', {
        recommendType
    });
}

/**
 * 获得商品 SPU 分页
 *
 * @param data
 * @returns {*}
 */
export function getSpuPage(data) {
    return request.get('app-api/product/spu/page', data, {
        noAuth: true // TODO 芋艿：后续要做调整
    });
}

/**
 * 查询商品
 *
 * @param id
 * @returns {*}
 */
export function getSpuDetail(id) {
    return request.get('app-api/product/spu/get-detail', {
        id
    }, {
        noAuth: true // TODO 芋艿：后续要做调整
    });
}
