package cn.iocoder.educate.framework.tenant.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 *
 * 多租户上下文 Holder
 *
 * @Author: j-sentinel
 * @Date: 2023/5/16 11:33
 */
public class TenantContextHolder {

    /**
     * 当前租户编号
     */
    private static final ThreadLocal<Long> TENANT_ID = new TransmittableThreadLocal<>();

    /**
     * 获得租户编号。
     *
     * @return 租户编号
     */
    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    public static void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

}
