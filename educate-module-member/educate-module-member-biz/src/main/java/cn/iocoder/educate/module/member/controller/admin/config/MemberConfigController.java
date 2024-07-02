package cn.iocoder.educate.module.member.controller.admin.config;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.module.member.controller.admin.config.vo.MemberConfigRespVO;
import cn.iocoder.educate.module.member.controller.admin.config.vo.MemberConfigSaveReqVO;
import cn.iocoder.educate.module.member.convert.config.MemberConfigConvert;
import cn.iocoder.educate.module.member.dal.dataobject.config.MemberConfigDO;
import cn.iocoder.educate.module.member.service.config.MemberConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/11/21 14:29
 */
@Tag(name = "管理后台 - 会员设置")
@RestController
@RequestMapping("/member/point/config")
@Validated
public class MemberConfigController {

    @Resource
    private MemberConfigService memberConfigService;

    @PutMapping("/save")
    @Operation(summary = "保存会员配置")
    @PreAuthorize("@lanxin.hasPermission('member:config:save')")
    public CommonResult<Boolean> saveConfig(@Valid @RequestBody MemberConfigSaveReqVO saveReqVO) {
        memberConfigService.saveConfig(saveReqVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员配置")
    @PreAuthorize("@lanxin.hasPermission('member:config:query')")
    public CommonResult<MemberConfigRespVO> getConfig() {
        MemberConfigDO config = memberConfigService.getConfig();
        return success(MemberConfigConvert.INSTANCE.convert(config));
    }

}
