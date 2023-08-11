package cn.iocoder.educate.module.system.service.sms;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.channel.SmsChannelCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.channel.SmsChannelPageReqVO;
import cn.iocoder.educate.module.system.controller.admin.sms.vo.channel.SmsChannelUpdateReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsChannelDO;

import javax.validation.Valid;
import java.util.List;

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

    /**
     * 创建短信渠道
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSmsChannel(@Valid SmsChannelCreateReqVO createReqVO);

    /**
     * 更新短信渠道
     *
     * @param updateReqVO 更新信息
     */
    void updateSmsChannel(@Valid SmsChannelUpdateReqVO updateReqVO);

    /**
     * 删除短信渠道
     *
     * @param id 编号
     */
    void deleteSmsChannel(Long id);

    /**
     * 获得所有短信渠道列表
     *
     * @return 短信渠道列表
     */
    List<SmsChannelDO> getSmsChannelList();

    /**
     * 获得短信渠道分页
     *
     * @param pageReqVO 分页查询
     * @return 短信渠道分页
     */
    PageResult<SmsChannelDO> getSmsChannelPage(SmsChannelPageReqVO pageReqVO);

}
