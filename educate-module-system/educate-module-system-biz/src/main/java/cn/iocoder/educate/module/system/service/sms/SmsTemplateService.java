package cn.iocoder.educate.module.system.service.sms;

import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsTemplateDO;

import java.util.Map;

/**
 * 短信模板 Service 接口
 *
 * @Author: 董伟豪
 * @Date: 2023/5/27 20:10
 */
public interface SmsTemplateService {

    /**
     * 初始化短信模板的本地缓存
     */
    void initLocalCache();

    /**
     * 获得短信模板，从缓存中
     *
     * @param code 模板编码
     * @return 短信模板
     */
    SmsTemplateDO getSmsTemplateByCodeFromCache(String code);

    /**
     * 格式化短信内容
     * @param content 短信模板的内容
     * @param templateParams 内容的参数
     * @return 格式化后的内容
     */
    String formatSmsTemplateContent(String content, Map<String, Object> templateParams);

    /**
     * 获得指定短信渠道下的短信模板数量
     *
     * @param channelId 短信渠道编号
     * @return 数量
     */
    Long countByChannelId(Long channelId);

}
