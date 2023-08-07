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


export const generateUUID = () => {
    if (typeof crypto === 'object') {
        if (typeof crypto.randomUUID === 'function') {
            return crypto.randomUUID()
        }
        if (typeof crypto.getRandomValues === 'function' && typeof Uint8Array === 'function') {
            const callback = (c) => {
                const num = Number(c)
                return (num ^ (crypto.getRandomValues(new Uint8Array(1))[0] & (15 >> (num / 4)))).toString(
                    16
                )
            }
            return '10000000-1000-4000-8000-100000000000'.replace(/[018]/g, callback)
        }
    }
    let timestamp = new Date().getTime()
    let performanceNow =
        (typeof performance !== 'undefined' && performance.now && performance.now() * 1000) || 0
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
        let random = Math.random() * 16
        if (timestamp > 0) {
            random = (timestamp + random) % 16 | 0
            timestamp = Math.floor(timestamp / 16)
        } else {
            random = (performanceNow + random) % 16 | 0
            performanceNow = Math.floor(performanceNow / 16)
        }
        return (c === 'x' ? random : (random & 0x3) | 0x8).toString(16)
    })
}
