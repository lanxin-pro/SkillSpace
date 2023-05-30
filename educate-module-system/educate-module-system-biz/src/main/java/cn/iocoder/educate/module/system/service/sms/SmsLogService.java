package cn.iocoder.educate.module.system.service.sms;

import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsTemplateDO;

import java.util.Map;

/**
 * 短信日志 Service 接口
 * 
 * @Author: 董伟豪
 * @Date: 2023/5/30 10:11
 */
public interface SmsLogService {

    /**
     * 创建短信日志
     * @param mobile 手机号
     * @param userId 用户Id
     * @param userType 用户类型
     * @param isSend 是否发送
     * @param template 短信模板
     * @param templateContent 短信内容
     * @param templateParams 短信参数
     * @return 发送日志编号
     */
    Long createSmsLog(String mobile, Long userId, Integer userType, Boolean isSend,
                      SmsTemplateDO template, String templateContent, Map<String, Object> templateParams);
}
