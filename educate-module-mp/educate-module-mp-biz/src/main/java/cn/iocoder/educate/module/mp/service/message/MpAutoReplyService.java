package cn.iocoder.educate.module.mp.service.message;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.autoreply.MpAutoReplyCreateReqVO;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.autoreply.MpAutoReplyUpdateReqVO;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.autoreply.MpMessagePageReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.message.MpAutoReplyDO;

/**
 * 公众号的自动回复 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/9/1 20:23
 */
public interface MpAutoReplyService {

    /**
     * 获得公众号自动回复分页
     *
     * @param mpMessagePageReqVO 分页请求
     * @return 自动回复分页结果
     */
    PageResult<MpAutoReplyDO> getAutoReplyPage(MpMessagePageReqVO mpMessagePageReqVO);

    /**
     * 获得公众号自动回复
     *
     * @param id 编号
     * @return 自动回复
     */
    MpAutoReplyDO getAutoReply(Long id);

    /**
     * 创建公众号自动回复
     *
     * @param createReqVO 创建请求
     * @return 自动回复的编号
     */
    Long createAutoReply(MpAutoReplyCreateReqVO createReqVO);

    /**
     * 更新公众号自动回复
     *
     * @param updateReqVO 更新请求
     */
    void updateAutoReply(MpAutoReplyUpdateReqVO updateReqVO);

    /**
     * 删除公众号自动回复
     *
     * @param id 自动回复的编号
     */
    void deleteAutoReply(Long id);

}
