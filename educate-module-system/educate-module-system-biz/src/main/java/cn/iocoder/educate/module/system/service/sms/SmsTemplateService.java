package cn.iocoder.educate.module.system.service.sms;

import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsTemplateDO;

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
}
