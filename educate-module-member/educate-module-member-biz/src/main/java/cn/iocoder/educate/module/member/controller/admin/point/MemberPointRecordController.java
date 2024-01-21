package cn.iocoder.educate.module.member.controller.admin.point;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.member.controller.admin.point.vo.recrod.MemberPointRecordPageReqVO;
import cn.iocoder.educate.module.member.controller.admin.point.vo.recrod.MemberPointRecordRespVO;
import cn.iocoder.educate.module.member.convert.point.MemberPointRecordConvert;
import cn.iocoder.educate.module.member.dal.dataobject.point.MemberPointRecordDO;
import cn.iocoder.educate.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.educate.module.member.service.point.MemberPointRecordService;
import cn.iocoder.educate.module.member.service.user.MemberUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author j-sentinel
 * @date 2024/1/19 19:42
 */
@Tag(name = "管理后台 - 签到记录")
@RestController
@RequestMapping("/member/point/record")
@Validated
public class MemberPointRecordController {

    @Resource
    private MemberPointRecordService memberPointRecordService;

    @Resource
    private MemberUserService memberUserService;

    @GetMapping("/page")
    @Operation(summary = "获得用户积分记录分页")
    @PreAuthorize("@lanxin.hasPermission('point:record:query')")
    public CommonResult<PageResult<MemberPointRecordRespVO>> getPointRecordPage(
            @Valid MemberPointRecordPageReqVO memberPointRecordPageReqVO) {
        // 执行分页查询
        PageResult<MemberPointRecordDO> pageResult = memberPointRecordService
                .getPointRecordPage(memberPointRecordPageReqVO);
        Set<Long> collectUserIds = pageResult.getList().stream()
                .map(MemberPointRecordDO::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<MemberUserDO> userList = memberUserService.getUserList(collectUserIds);
        // 拼接结果返回
        return CommonResult.success(MemberPointRecordConvert.INSTANCE.convertPage(pageResult, userList));
    }

}
