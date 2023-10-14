package cn.iocoder.educate.module.system.service.notify;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.enums.UserTypeEnum;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.module.system.dal.dataobject.notify.NotifyTemplateDO;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * 站内信发送 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/10/11 20:38
 */
@Service
@Validated
@Slf4j
public class NotifySendServiceImpl implements NotifySendService {

    @Resource
    private NotifyTemplateService notifyTemplateService;

    @Resource
    private NotifyMessageService notifyMessageService;

    @Override
    public Long sendSingleNotifyToAdmin(Long userId, String templateCode, Map<String, Object> templateParams) {
        return sendSingleNotify(userId, UserTypeEnum.ADMIN.getValue(), templateCode, templateParams);
    }

    @Override
    public Long sendSingleNotify(Long userId, Integer userType, String templateCode, Map<String, Object> templateParams) {
        // 校验模版
        NotifyTemplateDO template = validateNotifyTemplate(templateCode);
        if (Objects.equals(template.getStatus(), CommonStatusEnum.DISABLE.getStatus())) {
            log.info("[sendSingleNotify][模版({})已经关闭，无法给用户({}/{})发送]", templateCode, userId, userType);
            return null;
        }
        // 校验参数
        validateTemplateParams(template, templateParams);

        // 发送站内信
        String content = notifyTemplateService.formatNotifyTemplateContent(template.getContent(), templateParams);
        return notifyMessageService.createNotifyMessage(userId, userType, template, content, templateParams);
    }

    public NotifyTemplateDO validateNotifyTemplate(String templateCode) {
        // 获得站内信模板。考虑到效率，从缓存中获取
        NotifyTemplateDO template = notifyTemplateService.getNotifyTemplateByCodeFromCache(templateCode);
        // 站内信模板不存在
        if (template == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.NOTIFY_TEMPLATE_NOT_EXISTS);
        }
        return template;
    }

    /**
     * 校验站内信模版参数是否确实，是否存在多个参数或者没有给参数
     *
     * @param template 邮箱模板
     * @param templateParams 参数列表
     */
    public void validateTemplateParams(NotifyTemplateDO template, Map<String, Object> templateParams) {
        template.getParams().forEach(key -> {
            Object value = templateParams.get(key);
            if (value == null) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.NOTIFY_SEND_TEMPLATE_PARAM_MISS, key);
            }
        });
    }

}
