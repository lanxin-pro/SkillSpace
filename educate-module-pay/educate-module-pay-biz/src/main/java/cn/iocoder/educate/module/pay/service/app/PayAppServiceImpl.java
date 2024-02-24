package cn.iocoder.educate.module.pay.service.app;

import cn.iocoder.educate.module.pay.controller.admin.app.vo.PayAppPageReqVO;
import cn.iocoder.educate.module.pay.dal.dataobject.app.PayAppDO;
import cn.iocoder.educate.module.pay.dal.mysql.app.PayAppMapper;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 支付应用 Service 实现类
 *
 * @author j-sentinel
 * @date 2024/2/22 11:19
 */
@Service
@Validated
public class PayAppServiceImpl implements PayAppService {

    @Resource
    private PayAppMapper appMapper;

    @Override
    public PageResult<PayAppDO> getAppPage(PayAppPageReqVO payAppPageReqVO) {
        return appMapper.selectPage(payAppPageReqVO);
    }

}
