package cn.iocoder.educate.module.mp.framework.mp.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;

/**
 * 微信上下文 Context
 *
 * 目的：解决微信多公众号的问题，在 {@link WxMpMessageHandler} 实现类中，可以通过 {@link #getAppId()} 获取到当前的 appId
 *
 * @Author: j-sentinel
 * @Date: 2023/8/31 15:39
 */
public class MpContextHolder {

    /**
     * 微信公众号的 appId 上下文
     */
    private static final ThreadLocal<String> APPID = new TransmittableThreadLocal<>();

    public static void setAppId(String appId) {
        APPID.set(appId);
    }

    public static String getAppId() {
        return APPID.get();
    }

    public static void clear() {
        APPID.remove();
    }

}
