package cn.iocoder.educate.module.system.controller.admin.user;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.user.vo.UserPageItemRespVO;
import cn.iocoder.educate.module.system.controller.admin.user.vo.UserPageReqVO;
import cn.iocoder.educate.module.system.convert.user.UserConvert;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.service.dept.DeptService;
import cn.iocoder.educate.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.Valid;
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
        Map<Long, DeptDO> deptMap = deptService.getDeptMap(deptIds);
        // 拼接结果返回
        List<UserPageItemRespVO> userList = new ArrayList<>(pageResult.getList().size());
        pageResult.getList().forEach(user -> {
            UserPageItemRespVO respVO = UserConvert.INSTANCE.convert(user);
            respVO.setDept(UserConvert.INSTANCE.convert(deptMap.get(user.getDeptId())));
            userList.add(respVO);
        });
        return success(new PageResult<>(userList, pageResult.getTotal()));
    }
}
