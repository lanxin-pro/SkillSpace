package cn.iocoder.educate.module.system.controller.admin.permission;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.module.system.service.permission.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * 权限 Controller，提供赋予用户、角色的权限的 API 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/8/9 17:21
 */
@Tag(name = "管理后台 - 权限")
@RestController
@RequestMapping("/system/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    /**
     * 前端需要的是一个集合
     *
     * @param roleId
     * @return
     */
    @Operation(summary = "获得角色拥有的菜单编号")
    @Parameter(name = "roleId", description = "角色编号", required = true)
    @GetMapping("/list-role-resources")
    @PreAuthorize("@lanxin.hasPermission('system:permission:assign-role-menu')")
    public CommonResult<Set<Long>> listRoleMenus(Long roleId) {
        Set<Long> permissionServiceRoleMenuIds = permissionService.getRoleMenuIds(roleId);
        return success(permissionServiceRoleMenuIds);
    }

}
