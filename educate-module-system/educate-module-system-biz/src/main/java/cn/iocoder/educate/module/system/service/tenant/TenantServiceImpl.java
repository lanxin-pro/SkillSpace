package cn.iocoder.educate.module.system.service.tenant;

import cn.iocoder.educate.module.system.dal.dataobject.tenant.TenantDO;
import cn.iocoder.educate.module.system.dal.mysql.tenant.TenantMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/6/6 16:37
 */
@Slf4j
@Service
public class TenantServiceImpl implements TenantService{

    @Resource
    private TenantMapper tenantMapper;

    @Override
    public TenantDO getTenantByName(String name) {
        return tenantMapper.selectByName(name);
    }
}
