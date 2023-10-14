package cn.iocoder.educate.module.system.service.notify;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.notify.vo.message.NotifyMessageMyPageReqVO;
import cn.iocoder.educate.module.system.controller.admin.notify.vo.message.NotifyMessagePageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.notify.NotifyMessageDO;
import cn.iocoder.educate.module.system.dal.dataobject.notify.NotifyTemplateDO;

import java.util.List;
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

    /**
     * 获得站内信分页
     *
     * @param pageReqVO 分页查询
     * @return 站内信分页
     */
    PageResult<NotifyMessageDO> getNotifyMessagePage(NotifyMessagePageReqVO pageReqVO);

    /**
     * 获得站内信
     *
     * @param id 编号
     * @return 站内信
     */
    NotifyMessageDO getNotifyMessage(Long id);

    /**
     * 获得【我的】未读站内信列表
     *
     * @param userId   用户编号
     * @param userType 用户类型
     * @param size     数量
     * @return 站内信列表
     */
    List<NotifyMessageDO> getUnreadNotifyMessageList(Long userId, Integer userType, Integer size);

    /**
     * 统计用户未读站内信条数
     *
     * @param userId   用户编号
     * @param userType 用户类型
     * @return 返回未读站内信条数
     */
    Long getUnreadNotifyMessageCount(Long userId, Integer userType);

    /**
     * 获得【我的】站内信分页
     *
     * @param pageReqVO 分页查询
     * @param userId 用户编号
     * @param userType 用户类型
     * @return 站内信分页
     */
    PageResult<NotifyMessageDO> getMyMyNotifyMessagePage(NotifyMessageMyPageReqVO pageReqVO,
                                                         Long userId, Integer userType);
}
