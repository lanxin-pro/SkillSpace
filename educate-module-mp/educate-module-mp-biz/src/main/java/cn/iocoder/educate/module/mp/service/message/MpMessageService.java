package cn.iocoder.educate.module.mp.service.message;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

/**
 * 公众号消息 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/8/31 16:00
 */
public interface MpMessageService {

    /**
     * 从公众号，接收到粉丝消息
     *
     * @param appId 微信公众号 appId
     * @param wxMessage 消息
     */
    void receiveMessage(String appId, WxMpXmlMessage wxMessage);

}
