package cn.iocoder.educate.module.system.controller.admin.mail;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.account.*;
import cn.iocoder.educate.module.system.convert.mail.MailAccountConvert;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailAccountDO;
import cn.iocoder.educate.module.system.service.mail.MailAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/18 13:33
 */
@Tag(name = "管理后台 - 邮箱账号")
@RestController
@RequestMapping("/system/mail-account")
public class MailAccountController {

    @Resource
    private MailAccountService mailAccountService;

    @PostMapping("/create")
    @Operation(summary = "创建邮箱账号")
    @PreAuthorize("@lanxin.hasPermission('system:mail-account:create')")
    public CommonResult<Long> createMailAccount(@Valid @RequestBody MailAccountCreateReqVO createReqVO) {
        Long mailAccountServiceMailAccount = mailAccountService.createMailAccount(createReqVO);
        return success(mailAccountServiceMailAccount);
    }

    @PutMapping("/update")
    @Operation(summary = "修改邮箱账号")
    @PreAuthorize("@lanxin.hasPermission('system:mail-account:update')")
    public CommonResult<Boolean> updateMailAccount(@Valid @RequestBody MailAccountUpdateReqVO updateReqVO) {
        mailAccountService.updateMailAccount(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除邮箱账号")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@lanxin.hasPermission('system:mail-account:delete')")
    public CommonResult<Boolean> deleteMailAccount(@RequestParam Long id) {
        mailAccountService.deleteMailAccount(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得邮箱账号")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@lanxin.hasPermission('system:mail-account:get')")
    public CommonResult<MailAccountRespVO> getMailAccount(@RequestParam("id") Long id) {
        MailAccountDO mailAccountDO = mailAccountService.getMailAccount(id);
        return success(MailAccountConvert.INSTANCE.convert(mailAccountDO));
    }

    @GetMapping("/page")
    @Operation(summary = "获得邮箱账号分页")
    @PreAuthorize("@lanxin.hasPermission('system:mail-account:query')")
    public CommonResult<PageResult<MailAccountBaseVO>> getMailAccountPage(@Valid MailAccountPageReqVO pageReqVO) {
        PageResult<MailAccountDO> pageResult = mailAccountService.getMailAccountPage(pageReqVO);
        return success(MailAccountConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获得邮箱账号精简列表")
    public CommonResult<List<MailAccountSimpleRespVO>> getSimpleMailAccountList() {
        List<MailAccountDO> list = mailAccountService.getMailAccountList();
        return success(MailAccountConvert.INSTANCE.convertList02(list));
    }

}
