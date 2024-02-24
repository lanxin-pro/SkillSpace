package cn.iocoder.educate.module.pay.service.app;

import cn.iocoder.educate.module.pay.controller.admin.app.vo.PayAppPageReqVO;
import cn.iocoder.educate.module.pay.dal.dataobject.app.PayAppDO;
import cn.iocoder.educate.framework.common.pojo.PageResult;

/**
 * 支付应用 Service 接口
 *
 * @author j-sentinel
 * @date 2024/2/22 11:19
 */
public interface PayAppService {

    /**
     * 获得支付应用分页
     *
     * @param pageVO 分页查询
     * @return 支付应用分页
     */
    PageResult<PayAppDO> getAppPage(PayAppPageReqVO pageVO);
}
