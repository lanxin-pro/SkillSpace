package cn.iocoder.educate.module.mp.service.handler.message;

import cn.iocoder.educate.module.mp.framework.mp.core.context.MpContextHolder;
import cn.iocoder.educate.module.mp.service.message.MpMessageService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 保存微信消息的事件处理器
 *
 * @Author: j-sentinel
 * @Date: 2023/8/31 15:59
 */
@Slf4j
@Component
public class MessageReceiveHandler implements WxMpMessageHandler {

    @Resource
    private MpMessageService mpMessageService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                    WxMpService wxMpService, WxSessionManager sessionManager) {
        log.info("[handle][接收到请求消息，内容：{}]", wxMessage);
        mpMessageService.receiveMessage(MpContextHolder.getAppId(), wxMessage);
        return null;
    }

}
