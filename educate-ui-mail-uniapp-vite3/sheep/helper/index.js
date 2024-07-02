/**
 * 显示消息提示框
 * @param {String} title 提示的内容，长度与 icon 取值有关。
 * @param {Number} duration 提示的延迟时间，单位毫秒，默认：2000
 */
function toast(title, duration = 2000) {
    uni.showToast({
        title: String(title),
        icon: 'none',
        duration,
    });
}

export default {
    toast
}
