package cn.iocoder.educate.module.system.controller.admin.mail;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.template.*;
import cn.iocoder.educate.module.system.convert.mail.MailTemplateConvert;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailTemplateDO;
import cn.iocoder.educate.module.system.service.mail.MailTemplateService;
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
 * @Date: 2023/8/19 13:19
 */
@Tag(name = "管理后台 - 邮件模版")
@RestController
@RequestMapping("/system/mail-template")
public class MailTemplateController {

    @Resource
    private MailTemplateService mailTempleService;

    @PostMapping("/create")
    @Operation(summary = "创建邮件模版")
    @PreAuthorize("@lanxin.hasPermission('system:mail-template:create')")
    public CommonResult<Long> createMailTemplate(@Valid @RequestBody MailTemplateCreateReqVO createReqVO){
        Long mailTemplateId = mailTempleService.createMailTemplate(createReqVO);
        return success(mailTemplateId);
    }

    @PutMapping("/update")
    @Operation(summary = "修改邮件模版")
    @PreAuthorize("@lanxin.hasPermission('system:mail-template:update')")
    public CommonResult<Boolean> updateMailTemplate(@Valid @RequestBody MailTemplateUpdateReqVO mailTemplateUpdateReqVO){
        mailTempleService.updateMailTemplate(mailTemplateUpdateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除邮件模版")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@lanxin.hasPermission('system:mail-template:delete')")
    public CommonResult<Boolean> deleteMailTemplate(@RequestParam("id") Long id) {
        mailTempleService.deleteMailTemplate(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得邮件模版")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@lanxin.hasPermission('system:mail-template:get')")
    public CommonResult<MailTemplateRespVO> getMailTemplate(@RequestParam("id") Long id) {
        MailTemplateDO mailTemplateDO = mailTempleService.getMailTemplate(id);
        return success(MailTemplateConvert.INSTANCE.convert(mailTemplateDO));
    }

    @GetMapping("/page")
    @Operation(summary = "获得邮件模版分页")
    @PreAuthorize("@lanxin.hasPermission('system:mail-template:query')")
    public CommonResult<PageResult<MailTemplateRespVO>> getMailTemplatePage(@Valid MailTemplatePageReqVO pageReqVO) {
        PageResult<MailTemplateDO> pageResult = mailTempleService.getMailTemplatePage(pageReqVO);
        return success(MailTemplateConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获得邮件模版精简列表")
    public CommonResult<List<MailTemplateSimpleRespVO>> getSimpleTemplateList() {
        List<MailTemplateDO> mailTemplateList = mailTempleService.getMailTemplateList();
        return success(MailTemplateConvert.INSTANCE.convertList02(mailTemplateList));
    }

}
