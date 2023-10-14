package cn.iocoder.educate.module.system.service.notify;

import cn.iocoder.educate.module.system.dal.dataobject.notify.NotifyTemplateDO;

import java.util.Map;

/**
 * 站内信 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/10/14 12:31
 */
public interface NotifyMessageService {

    /**
     * 创建站内信
     *
     * @param userId 用户编号
     * @param userType 用户类型
     * @param template 模版信息
     * @param templateContent 模版内容
     * @param templateParams 模版参数
     * @return 站内信编号
     */
    Long createNotifyMessage(Long userId, Integer userType, NotifyTemplateDO template, String templateContent,
                             Map<String, Object> templateParams);

}
