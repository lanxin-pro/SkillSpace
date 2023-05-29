package cn.iocoder.educate.module.system.service.sms;

import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsChannelDO;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/28 19:12
 */
public interface SmsChannelService {

    /**
     * 初始化短信客户端
     */
    void initLocalCache();

    /**
     * 获得短信渠道
     * @param channelId 编号
     * @return 短信渠道
     */
    SmsChannelDO getSmsChannel(Long channelId);
}
