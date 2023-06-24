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

// -转驼峰
export function toCamelCase(str, upperCaseFirst) {
    str = (str || '').toLowerCase().replace(/-(.)/g, function (match, group1) {
        return group1.toUpperCase();
    });

    if (upperCaseFirst && str) {
        str = str.charAt(0).toUpperCase() + str.slice(1);
    }

    return str;
}
