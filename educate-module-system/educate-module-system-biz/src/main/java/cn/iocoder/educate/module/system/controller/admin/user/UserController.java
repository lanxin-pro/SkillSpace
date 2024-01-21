package cn.iocoder.educate.module.system.controller.admin.user;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageParam;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.excel.core.util.ExcelUtils;
import cn.iocoder.educate.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.educate.framework.operatelog.core.enums.OperateTypeEnum;
import cn.iocoder.educate.module.system.controller.admin.user.vo.user.*;
import cn.iocoder.educate.module.system.convert.dept.DeptConvert;
import cn.iocoder.educate.module.system.convert.user.UserConvert;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.service.dept.DeptService;
import cn.iocoder.educate.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/6 9:34
 */
@Tag(name = "管理后台 - 用户")
@RestController
@RequestMapping("/system/user")
@Validated
public class UserController {

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private DeptService deptService;

    @GetMapping("/page")
    @Operation(summary = "获得用户分页列表")
    public CommonResult<PageResult<UserPageItemRespVO>> getUserPage(@Valid UserPageReqVO reqVO) {
        // 获得用户分页列表
        PageResult<AdminUserDO> pageResult = adminUserService.getUserPage(reqVO);
        if(CollUtil.isEmpty(pageResult.getList())){
            // 返回空，total就是空
            return success(new PageResult<>(pageResult.getTotal()));
        }
        // 获得拼接需要的数据
        Collection<Long> deptIds = pageResult.getList()
                .stream()
                .map(AdminUserDO::getDeptId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        // 从dept表中查询对应的记录
        Map<Long, DeptDO> deptMap = deptService.getDeptMap(deptIds);
        // 拼接结果返回
        List<UserPageItemRespVO> userList = new ArrayList<>(pageResult.getList().size());
        pageResult.getList().forEach(user -> {
            UserPageItemRespVO respVO = UserConvert.INSTANCE.convert(user);
            DeptDO deptDO = deptMap.get(user.getDeptId());
            respVO.setDept(UserConvert.INSTANCE.convert(deptDO));
            userList.add(respVO);
        });
        return success(new PageResult<>(userList, pageResult.getTotal()));
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<UserRespVO> getUser(@RequestParam("id") Long id) {
        AdminUserDO user = adminUserService.getUser(id);
        // 获得部门数据
        DeptDO dept = deptService.getDept(user.getDeptId());
        return success(UserConvert.INSTANCE.convert(user).setDept(UserConvert.INSTANCE.convert(dept)));
    }

    @PostMapping("/create")
    @Operation(summary = "新增用户")
    public CommonResult<Long> createUser(@Valid @RequestBody UserCreateReqVO reqVO) {
        Long id = adminUserService.createUser(reqVO);
        return success(id);
    }

    @PutMapping("update")
    @Operation(summary = "修改用户")
    public CommonResult<Boolean> updateUser(@Valid @RequestBody UserUpdateReqVO reqVO) {
        adminUserService.updateUser(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> deleteUser(@RequestParam("id") Long id) {
        adminUserService.deleteUser(id);
        return success(true);
    }

    @PutMapping("/update-password")
    @Operation(summary = "重置用户密码")
    public CommonResult<Boolean> updateUserPassword(@Valid @RequestBody UserUpdatePasswordReqVO reqVO) {
        adminUserService.updateUserPassword(reqVO.getId(), reqVO.getPassword());
        return success(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "修改用户状态")
    public CommonResult<Boolean> updateUserStatus(@Valid @RequestBody UserUpdateStatusReqVO reqVO) {
        adminUserService.updateUserStatus(reqVO.getId(), reqVO.getStatus());
        return success(true);
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获取用户精简信息列表", description = "只包含被开启的用户，主要用于前端的下拉选项")
    public CommonResult<List<UserSimpleRespVO>> getSimpleUserList() {
        // 获用户列表，只要开启状态的
        List<AdminUserDO> list = adminUserService.getUserListByStatus(CommonStatusEnum.ENABLE.getStatus());
        // 排序后，返回给前端
        return success(UserConvert.INSTANCE.convertList04(list));
    }

    @GetMapping("/list-all-simple-dept")
    @Operation(summary = "获取用户精简信息列表 + 部门名称", description = "只包含被开启的用户，主要用于前端的下拉选项")
    public CommonResult<List<UserSimpleRespVO>> getSimpleUserSimpleDeptList() {
        // 获用户列表，只要开启状态的
        List<AdminUserDO> list = adminUserService.getUserListByStatus(CommonStatusEnum.ENABLE.getStatus());
        // 拼接结果返回
        List<UserSimpleRespVO> userList = new ArrayList<>(list.size());
        list.stream()
                .peek(user -> {
                    UserSimpleRespVO userSimpleRespVO = DeptConvert.INSTANCE.convert(user);
                    DeptDO dept = deptService.getDept(user.getDeptId());
                    userSimpleRespVO.setDeptName(dept.getName());
                    userList.add(userSimpleRespVO);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        userList.sort(Comparator.comparing(UserSimpleRespVO::getDeptName));
        // 排序后，返回给前端
        return success(UserConvert.INSTANCE.convertList05(userList));
    }

    @GetMapping("/export")
    @Operation(summary = "导出用户组")
    @PreAuthorize("@lanxin.hasPermission('system:user:export')")
    @OperateLog(type = OperateTypeEnum.EXPORT)
    public void exportUserList(@Validated UserPageReqVO exportReqVO,
                               HttpServletResponse response) throws IOException {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AdminUserDO> list = adminUserService.getUserPage(exportReqVO).getList();
        List<Long> collectDeptIds = list.stream().map(AdminUserDO::getDeptId).collect(Collectors.toList());
        Map<Long, DeptDO> deptMap = deptService.getDeptMap(collectDeptIds);
        List<UserExcelRespVO> userExcelRespVOS = UserConvert.INSTANCE.convertList(list, deptMap);
        // 输出 Excel
        ExcelUtils.write(response, "用户数据.xls", "数据", UserExcelRespVO.class, userExcelRespVOS);
    }

}
