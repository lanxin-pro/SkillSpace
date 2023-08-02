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


/**
 * element plus 的文件大小 Formatter 实现
 *
 * @param row 行数据
 * @param column 字段
 * @param cellValue 字段值
 */
// @ts-ignore
export const fileSizeFormatter = (row, column, cellValue) => {
    const fileSize = cellValue
    const unitArr = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
    const srcSize = parseFloat(fileSize)
    const index = Math.floor(Math.log(srcSize) / Math.log(1024))
    const size = srcSize / Math.pow(1024, index)
    const sizeStr = size.toFixed(2) //保留的小数位数
    return sizeStr + ' ' + unitArr[index]
}
