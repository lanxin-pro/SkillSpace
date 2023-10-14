package cn.iocoder.educate.module.system.service.notify;

import cn.iocoder.educate.module.system.dal.dataobject.notify.NotifyMessageDO;
import cn.iocoder.educate.module.system.dal.dataobject.notify.NotifyTemplateDO;
import cn.iocoder.educate.module.system.dal.mysql.notify.NotifyMessageMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 站内信 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/10/14 12:31
 */
@Service
@Validated
public class NotifyMessageServiceImpl implements NotifyMessageService {

    @Resource
    private NotifyMessageMapper notifyMessageMapper;

    /**
     * TODO j-sentinel 这里的代码需要发邮件的发邮件，发通知的发通知
     * @param userId 用户编号
     * @param userType 用户类型
     * @param template 模版信息
     * @param templateContent 模版内容
     * @param templateParams 模版参数
     * @return
     */
    @Override
    public Long createNotifyMessage(Long userId, Integer userType, NotifyTemplateDO template,
                                    String templateContent, Map<String, Object> templateParams) {
        NotifyMessageDO message = new NotifyMessageDO().setUserId(userId).setUserType(userType)
                .setTemplateId(template.getId()).setTemplateCode(template.getCode())
                .setTemplateType(template.getType()).setTemplateNickname(template.getNickname())
                .setTemplateContent(templateContent).setTemplateParams(templateParams).setReadStatus(false);
        notifyMessageMapper.insert(message);
        return message.getId();
    }

}
