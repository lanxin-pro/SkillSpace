package cn.iocoder.educate.module.system.controller.admin.tenant;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.educate.module.system.dal.dataobject.tenant.TenantDO;
import cn.iocoder.educate.module.system.service.tenant.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

/**
 * @Author: j-sentinel
 * @Date: 2023/6/6 15:15
 */
@Tag(name = "管理后台 - 租户")
@RestController
@RequestMapping("/system/tenant")
public class TenantController {

    @Resource
    private TenantService tenantService;

    @GetMapping("/get-id-by-name")
    @PermitAll
    @Operation(summary = "使用租户名，获得租户编号", description = "登录界面，根据用户的租户名，获得租户编号")
    @Parameter(name = "name", description = "租户名", required = true, example = "1024")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<Long> getTenantIdByName(@RequestParam("name")String name){
        TenantDO tenantDO = tenantService.getTenantByName(name);
        return CommonResult.success(tenantDO != null ? tenantDO.getId() : null);
    }
}
