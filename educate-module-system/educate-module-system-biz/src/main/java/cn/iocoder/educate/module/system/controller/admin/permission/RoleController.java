package cn.iocoder.educate.module.system.controller.admin.permission;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.role.*;
import cn.iocoder.educate.module.system.convert.permission.RoleConvert;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.educate.module.system.service.permission.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/8 17:18
 */
@Tag(name = "管理后台 - 角色")
@RestController
@RequestMapping("/system/role")
@Validated
public class RoleController {

    @Resource
    private RoleService roleService;

    @PostMapping("/create")
    @Operation(summary = "创建角色")
    @PreAuthorize("@lanxin.hasPermission('system:role:create')")
    public CommonResult<Long> createRole(@Valid @RequestBody RoleCreateReqVO roleCreateReqVO) {
        return success(roleService.createRole(roleCreateReqVO, null));
    }

    @PutMapping("/update")
    @Operation(summary = "修改角色")
    @PreAuthorize("@lanxin.hasPermission('system:role:update')")
    public CommonResult<Boolean> updateRole(@Valid @RequestBody RoleUpdateReqVO roleUpdateReqVO) {
        roleService.updateRole(roleUpdateReqVO);
        return success(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "修改角色状态")
    @PreAuthorize("@lanxin.hasPermission('system:role:update')")
    public CommonResult<Boolean> updateRoleStatus(@Valid @RequestBody RoleUpdateStatusReqVO roleUpdateStatusReqVO) {
        roleService.updateRoleStatus(roleUpdateStatusReqVO.getId(), roleUpdateStatusReqVO.getStatus());
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除角色")
    @Parameter(name = "id", description = "角色编号", required = true, example = "1024")
    @PreAuthorize("@lanxin.hasPermission('system:role:delete')")
    public CommonResult<Boolean> deleteRole(@RequestParam("id") Long id) {
        roleService.deleteRole(id);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得角色分页")
    @PreAuthorize("@lanxin.hasPermission('system:role:query')")
    public CommonResult<PageResult<RoleDO>> getRolePage(RolePageReqVO rolePageReqVO) {
        return success(roleService.getRolePage(rolePageReqVO));
    }

    @GetMapping("/get")
    @Operation(summary = "获得角色信息")
    @PreAuthorize("@lanxin.hasPermission('system:role:query')")
    public CommonResult<RoleRespVO> getRole(@RequestParam("id") Long id) {
        RoleDO role = roleService.getRole(id);
        RoleRespVO roleRespVO = RoleConvert.INSTANCE.convert(role);
        return success(roleRespVO);
    }

}
