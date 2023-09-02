package cn.iocoder.educate.module.mp.service.message;

import cn.iocoder.educate.framework.common.pojo.PageResult;
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

}
