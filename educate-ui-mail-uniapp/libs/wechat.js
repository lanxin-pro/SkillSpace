// #ifdef H5
class AuthWechat {

    /**
     * 判断是否在微信公众号的浏览器中
     */
    isWeixin() {
        return navigator.userAgent.toLowerCase().indexOf("micromessenger") !== -1;
    }

}

export default new AuthWechat();
// #endif
