package cn.iocoder.educate.module.system.controller.admin.dept;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.dept.DeptSimpleRespVO;
import cn.iocoder.educate.module.system.convert.dept.DeptConvert;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.educate.module.system.service.dept.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/3 17:32
 */
@Tag(name = "管理后台 - 部门")
@RestController
@RequestMapping("/system/dept")
@Validated
public class DeptController {

    @Resource
    private DeptService deptService;

    @GetMapping("/list-all-simple")
    @Operation(summary = "获取部门精简信息列表", description = "只包含被开启的部门，主要用于前端的下拉选项")
    public CommonResult<List<DeptSimpleRespVO>> getSimpleDeptList() {
        // 获得部门列表，只要开启状态的
        DeptListReqVO deptListReqVO = new DeptListReqVO();
        deptListReqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        List<DeptDO> list = deptService.getDeptList(deptListReqVO);
        // 排序后，返回给前端
        list.sort(Comparator.comparing(DeptDO::getSort));
        return success(DeptConvert.INSTANCE.convertList02(list));
    }
}
