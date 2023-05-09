import nprogress from 'nprogress'

/**
 * 浏览器全屏动画 -开启
 */
export function showFullLoading() {
    nprogress.start();
}
/**
 * 浏览器全屏动画 -结束
 */
export function hideFullLoading() {
    nprogress.done();
}
