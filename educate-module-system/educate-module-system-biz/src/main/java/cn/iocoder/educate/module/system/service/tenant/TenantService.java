package cn.iocoder.educate.module.system.service.tenant;

import cn.iocoder.educate.module.system.dal.dataobject.tenant.TenantDO;

/**
 * 租户 Service 接口
 *
 * @Author: 董伟豪
 * @Date: 2023/6/6 16:30
 */
public interface TenantService {

    /**
     * 获得名字对应的租户
     *
     * @param name 组户名
     * @return 租户
     */
    TenantDO getTenantByName(String name);
}
