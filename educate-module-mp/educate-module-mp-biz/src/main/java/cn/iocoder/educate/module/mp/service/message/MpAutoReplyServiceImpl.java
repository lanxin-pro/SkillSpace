package cn.iocoder.educate.module.mp.service.message;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.iocoder.educate.framework.common.exception.ErrorCode;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.autoreply.MpAutoReplyCreateReqVO;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.autoreply.MpAutoReplyUpdateReqVO;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.autoreply.MpMessagePageReqVO;
import cn.iocoder.educate.module.mp.convert.message.MpAutoReplyConvert;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.dal.dataobject.message.MpAutoReplyDO;
import cn.iocoder.educate.module.mp.dal.mysql.message.MpAutoReplyMapper;
import cn.iocoder.educate.module.mp.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.mp.enums.message.MpAutoReplyTypeEnum;
import cn.iocoder.educate.module.mp.framework.mp.core.util.MpUtils;
import cn.iocoder.educate.module.mp.service.account.MpAccountService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/1 20:23
 */
@Slf4j
@Service
@Validated
public class MpAutoReplyServiceImpl implements MpAutoReplyService {

    @Resource
    private MpAutoReplyMapper mpAutoReplyMapper;

    /**
     * 延迟加载，解决循环依赖的问题
     */
    @Resource
    @Lazy
    private MpAccountService mpAccountService;

    @Resource
    private Validator validator;

    @Override
    public PageResult<MpAutoReplyDO> getAutoReplyPage(MpMessagePageReqVO mpMessagePageReqVO) {
        return mpAutoReplyMapper.selectPage(mpMessagePageReqVO);
    }

    @Override
    public MpAutoReplyDO getAutoReply(Long id) {
        return mpAutoReplyMapper.selectById(id);
    }

    @Override
    public Long createAutoReply(MpAutoReplyCreateReqVO createReqVO) {
        // 第一步，校验数据
        if (createReqVO.getResponseMessageType() != null) {
            // 校验分组
            MpUtils.validateMessage(validator, createReqVO.getResponseMessageType(), createReqVO);
        }
        // 校验自动回复是否冲突
        validateAutoReplyConflict(null, createReqVO.getAccountId(), createReqVO.getType(),
                createReqVO.getRequestKeyword(), createReqVO.getRequestMessageType());

        // 第二步，插入数据
        MpAccountDO account = mpAccountService.getRequiredAccount(createReqVO.getAccountId());
        MpAutoReplyDO autoReply = MpAutoReplyConvert.INSTANCE.convert(createReqVO)
                .setAppId(account.getAppId());
        mpAutoReplyMapper.insert(autoReply);
        return autoReply.getId();
    }

    @Override
    public void updateAutoReply(MpAutoReplyUpdateReqVO updateReqVO) {
        // 第一步，校验数据
        if (updateReqVO.getResponseMessageType() != null) {
            MpUtils.validateMessage(validator, updateReqVO.getResponseMessageType(), updateReqVO);
        }
        MpAutoReplyDO autoReply = validateAutoReplyExists(updateReqVO.getId());
        validateAutoReplyConflict(updateReqVO.getId(), autoReply.getAccountId(), updateReqVO.getType(),
                updateReqVO.getRequestKeyword(), updateReqVO.getRequestMessageType());

        // 第二步，更新数据
        MpAutoReplyDO updateObj = MpAutoReplyConvert.INSTANCE.convert(updateReqVO)
                // 避免前端传递，更新着两个字段
                .setAccountId(null).setAppId(null);
        mpAutoReplyMapper.updateById(updateObj);
    }

    @Override
    public void deleteAutoReply(Long id) {
        // 校验自动回复是否存在
        validateAutoReplyExists(id);

        // 删除自动回复
        mpAutoReplyMapper.deleteById(id);
    }

    @Override
    public WxMpXmlOutMessage replyForMessage(String appId, WxMpXmlMessage wxMessage) {
        return null;
    }

    /**
     * 校验自动回复是否冲突
     *
     * 不同的 type，会有不同的逻辑：
     * 1. type = SUBSCRIBE 时，不允许有其他的自动回复
     * 2. type = MESSAGE 时，校验 requestMessageType 已经存在自动回复
     * 3. type = KEYWORD 时，校验 keyword 已经存在自动回复
     *
     * @param id 自动回复编号
     * @param accountId 公众号账号的编号
     * @param type 类型
     * @param requestKeyword 请求关键词
     * @param requestMessageType 请求消息类型
     */
    private void validateAutoReplyConflict(Long id, Long accountId, Integer type,
                                           String requestKeyword, String requestMessageType) {
        // 获得已经存在的自动回复
        MpAutoReplyDO autoReply = null;
        ErrorCode errorCode = null;
        if (MpAutoReplyTypeEnum.SUBSCRIBE.getType().equals(type)) {
            // 关注时回复只能有一个
            autoReply = mpAutoReplyMapper.selectByAccountIdAndSubscribe(accountId);
            errorCode = ErrorCodeConstants.AUTO_REPLY_ADD_SUBSCRIBE_FAIL_EXISTS;
        } else if (MpAutoReplyTypeEnum.MESSAGE.getType().equals(type)) {
            autoReply = mpAutoReplyMapper.selectByAccountIdAndMessage(accountId, requestMessageType);
            errorCode = ErrorCodeConstants.AUTO_REPLY_ADD_MESSAGE_FAIL_EXISTS;
        } else if (MpAutoReplyTypeEnum.KEYWORD.getType().equals(type)) {
            autoReply = mpAutoReplyMapper.selectByAccountIdAndKeyword(accountId, requestKeyword);
            errorCode = ErrorCodeConstants.AUTO_REPLY_ADD_KEYWORD_FAIL_EXISTS;
        }
        if (autoReply == null) {
            return;
        }

        // 存在冲突，抛出业务异常
        // 情况一，新增（id == null），存在记录，说明冲突
        if (id == null
                // 情况二，修改（id != null），id 不匹配，说明冲突
                || ObjUtil.notEqual(id, autoReply.getId())) {
            throw ServiceExceptionUtil.exception(errorCode);
        }
    }

    private MpAutoReplyDO validateAutoReplyExists(Long id) {
        MpAutoReplyDO autoReply = mpAutoReplyMapper.selectById(id);
        if (autoReply == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTO_REPLY_NOT_EXISTS);
        }
        return autoReply;
    }

}
