// 视频格式后缀列表
const VIDEO_SUFFIX_LIST = ['.avi', '.mp4']
/**
 * 转换商品轮播的链接列表：根据链接的后缀，判断是视频链接还是图片链接
 *
 * @param {string[]} urlList 链接列表
 * @return {{src: string, type: 'video' | 'image' }[]}  转换后的链接列表
 */
export function formatGoodsSwiper(urlList) {
    return urlList.map((url, key) => {
        const isVideo = VIDEO_SUFFIX_LIST.some(suffix => url.includes(suffix));
        const type = isVideo ? 'video' :'image'
        const src = ''
        return { type, src }
    });
}
