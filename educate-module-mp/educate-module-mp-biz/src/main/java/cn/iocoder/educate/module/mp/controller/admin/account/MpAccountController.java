package cn.iocoder.educate.module.mp.controller.admin.account;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.account.vo.MpAccountCreateReqVO;
import cn.iocoder.educate.module.mp.controller.admin.account.vo.MpAccountPageReqVO;
import cn.iocoder.educate.module.mp.controller.admin.account.vo.MpAccountRespVO;
import cn.iocoder.educate.module.mp.controller.admin.account.vo.MpAccountUpdateReqVO;
import cn.iocoder.educate.module.mp.convert.account.MpAccountConvert;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.service.account.MpAccountService;
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
 * @Date: 2023/8/31 11:40
 */
@Tag(name = "管理后台 - 公众号账号")
@RestController
@RequestMapping("/mp/account")
@Validated
public class MpAccountController {

    @Resource
    private MpAccountService mpAccountService;

    @PostMapping("/create")
    @Operation(summary = "创建公众号账号")
    @PreAuthorize("@lanxin.hasPermission('mp:account:create')")
    public CommonResult<Long> createAccount(@Valid @RequestBody MpAccountCreateReqVO mpAccountCreateReqVO) {
        return success(mpAccountService.createAccount(mpAccountCreateReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新公众号账号")
    @PreAuthorize("@lanxin.hasPermission('mp:account:update')")
    public CommonResult<Boolean> updateAccount(@Valid @RequestBody MpAccountUpdateReqVO updateReqVO) {
        mpAccountService.updateAccount(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除公众号账号")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@lanxin.hasPermission('mp:account:delete')")
    public CommonResult<Boolean> deleteAccount(@RequestParam("id") Long id) {
        mpAccountService.deleteAccount(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得公众号账号")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@lanxin.hasPermission('mp:account:query')")
    public CommonResult<MpAccountRespVO> getAccount(@RequestParam("id") Long id) {
        MpAccountDO wxAccount = mpAccountService.getAccount(id);
        return success(MpAccountConvert.INSTANCE.convert(wxAccount));
    }

    @GetMapping("/page")
    @Operation(summary = "获得公众号账号分页")
    @PreAuthorize("@lanxin.hasPermission('mp:account:query')")
    public CommonResult<PageResult<MpAccountRespVO>> getAccountPage(@Valid MpAccountPageReqVO pageVO) {
        PageResult<MpAccountDO> pageResult = mpAccountService.getAccountPage(pageVO);
        return success(MpAccountConvert.INSTANCE.convertPage(pageResult));
    }

    @PutMapping("/generate-qr-code")
    @Operation(summary = "生成公众号二维码")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@lanxin.hasPermission('mp:account:qr-code')")
    public CommonResult<Boolean> generateAccountQrCode(@RequestParam("id") Long id) {
        mpAccountService.generateAccountQrCode(id);
        return success(true);
    }

}
