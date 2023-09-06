package cn.iocoder.educate.module.mp.service.message;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.message.MpMessagePageReqVO;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.message.MpMessageSendReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.message.MpMessageDO;
import cn.iocoder.educate.module.mp.dal.mysql.message.MpMessageMapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
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

    @Override
    public void receiveMessage(String appId, WxMpXmlMessage wxMessage) {

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
