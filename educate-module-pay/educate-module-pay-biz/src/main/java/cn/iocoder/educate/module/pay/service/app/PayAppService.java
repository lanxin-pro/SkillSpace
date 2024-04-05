package cn.iocoder.educate.module.pay.service.app;

import cn.iocoder.educate.module.pay.controller.admin.app.vo.PayAppPageReqVO;
import cn.iocoder.educate.module.pay.dal.dataobject.app.PayAppDO;
import cn.iocoder.educate.framework.common.pojo.PageResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    /**
     * 获得支付应用
     *
     * @param id 编号
     * @return 支付应用
     */
    PayAppDO getApp(Long id);

    /**
     * 获得支付应用列表
     *
     * @param ids 编号
     * @return 支付应用列表
     */
    List<PayAppDO> getAppList(Collection<Long> ids);

    /**
     * 获得指定编号的商户 Map
     *
     * @param ids 应用编号集合
     * @return 商户 Map
     */
    default Map<Long, PayAppDO> getAppMap(List<Long> ids) {
        List<PayAppDO> list = getAppList(ids);
        return list.stream().collect(Collectors.toMap(PayAppDO::getId, Function.identity(), (v1,v2) -> v1));
    }
}
