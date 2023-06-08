package cn.iocoder.educate.module.system.service.sms;

import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsLogDO;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsTemplateDO;
import cn.iocoder.educate.module.system.dal.mysql.sms.SmsLogMapper;
import cn.iocoder.educate.module.system.enums.sms.SmsReceiveStatusEnum;
import cn.iocoder.educate.module.system.enums.sms.SmsSendStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/30 10:11
 */
@Slf4j
@Service
public class SmsLogServiceImpl implements SmsLogService{

    @Resource
    private SmsLogMapper smsLogMapper;

    @Override
    public Long createSmsLog(String mobile, Long userId, Integer userType, Boolean isSend, SmsTemplateDO template, String templateContent, Map<String, Object> templateParams) {
        SmsLogDO.SmsLogDOBuilder logBuilder = SmsLogDO.builder();

        logBuilder.sendStatus(Objects.equals(isSend, true) ? SmsSendStatusEnum.INIT.getStatus()
                : SmsSendStatusEnum.IGNORE.getStatus());
        // 设置手机相关字段
        logBuilder.mobile(mobile).userId(userId).userType(userType);
        // 设置模板相关字段
        logBuilder.templateId(template.getId()).templateCode(template.getCode()).templateType(template.getType());
        logBuilder.templateContent(templateContent).templateParams(templateParams)
                .apiTemplateId(template.getApiTemplateId());
        // 设置渠道相关字段
        logBuilder.channelId(template.getChannelId()).channelCode(template.getChannelCode());
        // 设置接收相关字段
        logBuilder.receiveStatus(SmsReceiveStatusEnum.INIT.getStatus());

        // 插入数据库
        SmsLogDO logDO = logBuilder.build();
        smsLogMapper.insert(logDO);
        return logDO.getId();
    }
}