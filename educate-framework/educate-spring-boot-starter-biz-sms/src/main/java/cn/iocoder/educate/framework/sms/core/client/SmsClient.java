package cn.iocoder.educate.framework.sms.core.client;

import cn.iocoder.educate.framework.common.core.KeyValue;
import cn.iocoder.educate.framework.sms.core.client.dto.SmsSendRespDTO;

import java.util.List;

/**
 * 短信客户端，用于对接各短信平台的 SDK，实现短信发送等功能
 *
 * @Author: 董伟豪
 * @Date: 2023/5/29 19:45
 */
public interface SmsClient {

    /**
     * 发送消息
     *
     * @param logId 日志编号
     * @param mobile 手机号
     * @param apiTemplateId 短信 API 的模板编号
     * @param templateParams 短信模板参数。通过 List 数组，保证参数的顺序
     * @return 短信发送结果
     */
    SmsCommonResult<SmsSendRespDTO> sendSms(Long logId, String mobile, String apiTemplateId,
                                            List<KeyValue<String, Object>> templateParams);
}
