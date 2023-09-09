package cn.iocoder.educate.module.mp.service.message;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.message.MpMessagePageReqVO;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.message.MpMessageSendReqVO;
import cn.iocoder.educate.module.mp.convert.message.MpMessageConvert;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.dal.dataobject.message.MpMessageDO;
import cn.iocoder.educate.module.mp.dal.dataobject.user.MpUserDO;
import cn.iocoder.educate.module.mp.dal.mysql.message.MpMessageMapper;
import cn.iocoder.educate.module.mp.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.mp.enums.message.MpMessageSendFromEnum;
import cn.iocoder.educate.module.mp.framework.mp.core.MpServiceFactory;
import cn.iocoder.educate.module.mp.framework.mp.core.util.MpUtils;
import cn.iocoder.educate.module.mp.service.account.MpAccountService;
import cn.iocoder.educate.module.mp.service.user.MpUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Validator;

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

    @Resource
    private Validator validator;

    /**
     * 延迟加载，解决循环依赖的问题
     */
    @Resource
    @Lazy
    private MpServiceFactory mpServiceFactory;

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
        // 校验消息格式
        MpUtils.validateMessage(validator, sendReqVO.getType(), sendReqVO);

        // 获得关联信息
        MpUserDO user = mpUserService.getRequiredUser(sendReqVO.getUserId());
        MpAccountDO account = mpAccountService.getRequiredAccount(user.getAccountId());

        // 发送客服消息
        WxMpKefuMessage wxMessage = MpMessageConvert.INSTANCE.convert(sendReqVO, user);
        WxMpService mpService = mpServiceFactory.getRequiredMpService(user.getAppId());
        try {
            mpService.getKefuService().sendKefuMessageWithResponse(wxMessage);
        } catch (WxErrorException e) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MESSAGE_SEND_FAIL, e.getError().getErrorMsg());
        }

        // 记录消息
        MpMessageDO message = MpMessageConvert.INSTANCE.convert(wxMessage, account, user)
                .setSendFrom(MpMessageSendFromEnum.MP_TO_USER.getFrom());
        mpMessageMapper.insert(message);
        return message;
    }

}
