package cn.iocoder.educate.module.mp.convert.message;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.message.MpMessageRespVO;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.message.MpMessageSendReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.dal.dataobject.message.MpMessageDO;
import cn.iocoder.educate.module.mp.dal.dataobject.user.MpUserDO;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/6 11:43
 */
@Mapper
public interface MpMessageConvert {

    MpMessageConvert INSTANCE = Mappers.getMapper(MpMessageConvert.class);

    MpMessageRespVO convert(MpMessageDO bean);

    default MpMessageDO convert(WxMpXmlMessage wxMessage, MpAccountDO account, MpUserDO user) {
        MpMessageDO message = convert(wxMessage);
        if (account != null) {
            message.setAccountId(account.getId()).setAppId(account.getAppId());
        }
        if (user != null) {
            message.setUserId(user.getId()).setOpenid(user.getOpenid());
        }
        return message;
    }
    @Mappings(value = {
            @Mapping(source = "msgType", target = "type"),
            @Mapping(target = "createTime", ignore = true),
    })
    MpMessageDO convert(WxMpXmlMessage bean);

    List<MpMessageRespVO> convertList(List<MpMessageDO> list);

    PageResult<MpMessageRespVO> convertPage(PageResult<MpMessageDO> page);

    default WxMpKefuMessage convert(MpMessageSendReqVO sendReqVO, MpUserDO user) {
        me.chanjar.weixin.mp.builder.kefu.BaseBuilder<?> builder;
        // 个性化字段
        switch (sendReqVO.getType()) {
            case WxConsts.KefuMsgType.TEXT:
                builder = WxMpKefuMessage.TEXT().content(sendReqVO.getContent());
                break;
            case WxConsts.KefuMsgType.IMAGE:
                builder = WxMpKefuMessage.IMAGE().mediaId(sendReqVO.getMediaId());
                break;
            case WxConsts.KefuMsgType.VOICE:
                builder = WxMpKefuMessage.VOICE().mediaId(sendReqVO.getMediaId());
                break;
            case WxConsts.KefuMsgType.VIDEO:
                builder = WxMpKefuMessage.VIDEO().mediaId(sendReqVO.getMediaId())
                        .title(sendReqVO.getTitle()).description(sendReqVO.getDescription());
                break;
            case WxConsts.KefuMsgType.NEWS:
                builder = WxMpKefuMessage.NEWS().articles(convertList03(sendReqVO.getArticles()));
                break;
            case WxConsts.KefuMsgType.MUSIC:
                builder = WxMpKefuMessage.MUSIC().title(sendReqVO.getTitle()).description(sendReqVO.getDescription())
                        .thumbMediaId(sendReqVO.getThumbMediaId())
                        .musicUrl(sendReqVO.getMusicUrl()).hqMusicUrl(sendReqVO.getHqMusicUrl());
                break;
            default:
                throw new IllegalArgumentException("不支持的消息类型：" + sendReqVO.getType());
        }
        // 通用字段
        builder.toUser(user.getOpenid());
        return builder.build();
    }
    List<WxMpKefuMessage.WxArticle> convertList03(List<MpMessageDO.Article> list);

    default MpMessageDO convert(WxMpKefuMessage wxMessage, MpAccountDO account, MpUserDO user) {
        MpMessageDO message = convert(wxMessage);
        if (account != null) {
            message.setAccountId(account.getId()).setAppId(account.getAppId());
        }
        if (user != null) {
            message.setUserId(user.getId()).setOpenid(user.getOpenid());
        }
        return message;
    }
    @Mappings(value = {
            @Mapping(source = "msgType", target = "type"),
            @Mapping(target = "createTime", ignore = true),
    })
    MpMessageDO convert(WxMpKefuMessage bean);
}
