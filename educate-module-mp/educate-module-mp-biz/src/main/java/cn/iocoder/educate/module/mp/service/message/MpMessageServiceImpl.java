package cn.iocoder.educate.module.mp.service.message;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 粉丝消息 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/8/31 16:00
 */
@Service
@Validated
@Slf4j
public class MpMessageServiceImpl implements MpMessageService {
    @Override
    public void receiveMessage(String appId, WxMpXmlMessage wxMessage) {

    }
}
