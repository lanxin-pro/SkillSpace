package cn.iocoder.educate.module.mp.service.message;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.message.MpMessagePageReqVO;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.message.MpMessageSendReqVO;
import cn.iocoder.educate.module.mp.convert.message.MpMessageConvert;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.dal.dataobject.message.MpMessageDO;
import cn.iocoder.educate.module.mp.dal.dataobject.user.MpUserDO;
import cn.iocoder.educate.module.mp.dal.mysql.message.MpMessageMapper;
import cn.iocoder.educate.module.mp.enums.message.MpMessageSendFromEnum;
import cn.iocoder.educate.module.mp.service.account.MpAccountService;
import cn.iocoder.educate.module.mp.service.user.MpUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 粉丝消息 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/8/31 16:00
 */
@Service
@Validated
@Slf4j
public class MpMessageServiceImpl implements MpMessageService {

    @Resource
    private MpMessageMapper mpMessageMapper;

    /**
     * 延迟加载，避免循环依赖
     */
    @Resource
    @Lazy
    private MpAccountService mpAccountService;

    @Resource
    @Lazy
    private MpUserService mpUserService;

    @Override
    public void receiveMessage(String appId, WxMpXmlMessage wxMessage) {
        // 获得关联信息
        MpAccountDO account = mpAccountService.getAccountFromCache(appId);
        Assert.notNull(account, "公众号账号({}) 不存在", appId);
        MpUserDO user = mpUserService.getUser(appId, wxMessage.getFromUser());
        Assert.notNull(user, "公众号粉丝({}/{}) 不存在", appId, wxMessage.getFromUser());

        // 记录消息
        MpMessageDO message = MpMessageConvert.INSTANCE.convert(wxMessage, account, user)
                .setSendFrom(MpMessageSendFromEnum.USER_TO_MP.getFrom());
        mpMessageMapper.insert(message);
    }

    @Override
    public PageResult<MpMessageDO> getMessagePage(MpMessagePageReqVO pageReqVO) {
        return mpMessageMapper.selectPage(pageReqVO);
    }

    @Override
    public MpMessageDO sendKefuMessage(MpMessageSendReqVO sendReqVO) {
        return null;
    }

}
