// 视频格式后缀列表
const VIDEO_SUFFIX_LIST = ['.avi', '.mp4']

/**
 * 转换商品轮播的链接列表：根据链接的后缀，判断是视频链接还是图片链接
 *
 * @param {string[]} urlList 链接列表
 * @return {{src: string, type: 'video' | 'image' }[]}  转换后的链接列表
 */
export function formatGoodsSwiper(urlList) {
    return urlList.filter(url => url).map((url, key) => {
        const isVideo = VIDEO_SUFFIX_LIST.some(suffix => url.includes(suffix))
        const type = isVideo ? 'video' : 'image'
        const src = url
        return { type, src }
    });
}

/**
 * 将分转成元
 *
 * @param price 分，例如说 100 分
 * @returns {string} 元，例如说 1.00 元
 */
export function fen2yuan(price) {
    return (price / 100.0).toFixed(2)
}

/**
 * 从商品 SKU 数组中，转换出商品属性的数组
 *
 * 类似结构：[{
 *    id: // 属性的编号
 *    name: // 属性的名字
 *    values: [{
 *      id: // 属性值的编号
 *      name: // 属性值的名字
 *    }]
 * }]
 *
 * @param skus 商品 SKU 数组
 */
export function convertProductPropertyList(skus) {
    let result = [];
    for (const sku of skus) {
        if (!sku.properties) {
            continue
        }
        for (const property of sku.properties) {
            // ① 先处理属性
            let resultProperty = result.find(item => item.id === property.propertyId)
            if (!resultProperty) {
                resultProperty = {
                    id: property.propertyId,
                    name: property.propertyName,
                    values: []
                }
                result.push(resultProperty)
            }
            // ② 再处理属性值
            let resultValue = resultProperty.values.find(item => item.id === property.valueId)
            if (!resultValue) {
                resultProperty.values.push({
                    id: property.valueId,
                    name: property.valueName
                })
            }
        }
    }
    return result;
}
