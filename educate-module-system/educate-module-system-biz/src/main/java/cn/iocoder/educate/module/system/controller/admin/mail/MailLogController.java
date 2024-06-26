package cn.iocoder.educate.module.system.controller.admin.mail;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.log.MailLogPageReqVO;
import cn.iocoder.educate.module.system.controller.admin.mail.vo.log.MailLogRespVO;
import cn.iocoder.educate.module.system.convert.mail.MailLogConvert;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailLogDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.service.mail.MailLogService;
import cn.iocoder.educate.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/19 16:24
 */
@Tag(name = "管理后台 - 邮件日志")
@RestController
@RequestMapping("/system/mail-log")
public class MailLogController {

    @Resource
    private MailLogService mailLogService;

    @Resource
    private AdminUserService adminUserService;

    @GetMapping("/page")
    @Operation(summary = "获得邮箱日志分页")
    @PreAuthorize("@lanxin.hasPermission('system:mail-log:query')")
    public CommonResult<PageResult<MailLogRespVO>> getMailLogPage(@Valid MailLogPageReqVO mailLogPageReqVO) {
        PageResult<MailLogDO> pageResult = mailLogService.getMailLogPage(mailLogPageReqVO);
        // 拼接结果返回
        List<MailLogRespVO> userList = new ArrayList<>(pageResult.getList().size());
        pageResult.getList().forEach(log -> {
            MailLogRespVO convertVo = MailLogConvert.INSTANCE.convert(log);
            String userNickname = adminUserService.getUserNickname(log.getUserId());
            convertVo.setNickname(userNickname);
            userList.add(convertVo);
        });
        return success(new PageResult<>(userList,pageResult.getTotal()));
    }

    @GetMapping("/get")
    @Operation(summary = "获得邮箱日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@lanxin.hasPermission('system:mail-log:query')")
    public CommonResult<MailLogRespVO> getMailTemplate(@RequestParam("id") Long id) {
        MailLogDO mailLogDO = mailLogService.getMailLog(id);
        return success(MailLogConvert.INSTANCE.convert(mailLogDO));
    }

}
