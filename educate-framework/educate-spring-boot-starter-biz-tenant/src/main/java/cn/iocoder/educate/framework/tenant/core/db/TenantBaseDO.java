package cn.iocoder.educate.framework.tenant.core.db;

import cn.iocoder.educate.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/12 20:38
 * 拓展多租户的 BaseDO 基类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantBaseDO extends BaseDO {

    /**
     * 多租户编号
     */
    private Long tenantId;
}
