package cn.iocoder.educate.module.mp.service.message;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.message.MpMessagePageReqVO;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.message.MpMessageSendReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.message.MpMessageDO;
import cn.iocoder.educate.module.mp.service.message.bo.MpMessageSendOutReqBO;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * 公众号消息 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/8/31 16:00
 */
public interface MpMessageService {

    /**
     * 从公众号，接收到粉丝消息
     *
     * @param appId 微信公众号 appId
     * @param wxMessage 消息
     */
    void receiveMessage(String appId, WxMpXmlMessage wxMessage);

    /**
     * 获得公众号消息分页
     *
     * @param pageReqVO 分页查询
     * @return 公众号消息分页
     */
    PageResult<MpMessageDO> getMessagePage(MpMessagePageReqVO pageReqVO);

    /**
     * 使用公众号，给粉丝发送【客服】消息
     *
     * 注意，该方法会真实发送消息
     *
     * @param sendReqVO 消息内容
     * @return 消息
     */
    MpMessageDO sendKefuMessage(MpMessageSendReqVO sendReqVO);

    /**
     * 使用公众号，给粉丝回复消息
     *
     * 例如说：自动回复、客服消息、菜单回复消息等场景
     *
     * 注意，该方法只是返回 WxMpXmlOutMessage 对象，不会真的发送消息
     *
     * @param sendReqBO 消息内容
     * @return 微信回复消息 XML
     */
    WxMpXmlOutMessage sendOutMessage(MpMessageSendOutReqBO sendReqBO);

}
