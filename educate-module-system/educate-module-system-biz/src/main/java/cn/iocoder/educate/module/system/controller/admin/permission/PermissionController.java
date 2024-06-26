package cn.iocoder.educate.module.system.controller.admin.permission;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.permission.PermissionAssignRoleMenuReqVO;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.permission.PermissionAssignUserRoleReqVO;
import cn.iocoder.educate.module.system.service.permission.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/assign-role-menu")
    @Operation(summary = "赋予角色菜单")
    @PreAuthorize("@lanxin.hasPermission('system:permission:assign-role-menu')")
    public CommonResult<Boolean> assignRoleMenu(
            @Validated @RequestBody PermissionAssignRoleMenuReqVO permissionAssignRoleMenuReqVO) {
        // 执行菜单的分配
        permissionService.assignRoleMenu(permissionAssignRoleMenuReqVO.getRoleId(),
                permissionAssignRoleMenuReqVO.getMenuIds());
        return success(true);
    }

    @Operation(summary = "获得管理员拥有的角色编号列表")
    @Parameter(name = "userId", description = "用户编号", required = true)
    @GetMapping("/list-user-roles")
    @PreAuthorize("@lanxin.hasPermission('system:permission:assign-user-role')")
    public CommonResult<Set<Long>> listAdminRoles(@RequestParam("userId") Long userId) {
        Set<Long> userRoleIdListByUserId = permissionService.getUserRoleIdListByUserId(userId);
        return success(userRoleIdListByUserId);
    }

    @Operation(summary = "赋予用户角色")
    @PostMapping("/assign-user-role")
    @PreAuthorize("@lanxin.hasPermission('system:permission:assign-user-role')")
    public CommonResult<Boolean> assignUserRole
            (@Validated @RequestBody PermissionAssignUserRoleReqVO permissionAssignUserRoleReqVO) {
        permissionService.assignUserRole(permissionAssignUserRoleReqVO.getUserId(),
                permissionAssignUserRoleReqVO.getRoleIds());
        return success(true);
    }

}
