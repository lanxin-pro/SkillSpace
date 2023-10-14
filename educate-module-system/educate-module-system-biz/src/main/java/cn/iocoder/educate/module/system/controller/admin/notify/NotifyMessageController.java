package cn.iocoder.educate.module.system.controller.admin.notify;

import cn.iocoder.educate.framework.common.enums.UserTypeEnum;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.notify.vo.message.NotifyMessageMyPageReqVO;
import cn.iocoder.educate.module.system.controller.admin.notify.vo.message.NotifyMessagePageReqVO;
import cn.iocoder.educate.module.system.controller.admin.notify.vo.message.NotifyMessageRespVO;
import cn.iocoder.educate.module.system.convert.notify.NotifyMessageConvert;
import cn.iocoder.educate.module.system.dal.dataobject.notify.NotifyMessageDO;
import cn.iocoder.educate.module.system.service.notify.NotifyMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;
import static cn.iocoder.educate.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

/**
 * @Author: j-sentinel
 * @Date: 2023/10/11 20:27
 */
@Tag(name = "管理后台 - 我的站内信")
@RestController
@RequestMapping("/system/notify-message")
@Validated
public class NotifyMessageController {

    @Resource
    private NotifyMessageService notifyMessageService;

    // ========== 管理所有的站内信 ==========

    @GetMapping("/get")
    @Operation(summary = "获得站内信")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@lanxin.hasPermission('system:notify-message:query')")
    public CommonResult<NotifyMessageRespVO> getNotifyMessage(@RequestParam("id") Long id) {
        NotifyMessageDO notifyMessage = notifyMessageService.getNotifyMessage(id);
        return success(NotifyMessageConvert.INSTANCE.convert(notifyMessage));
    }

    @GetMapping("/page")
    @Operation(summary = "获得站内信分页")
    @PreAuthorize("@lanxin.hasPermission('system:notify-message:query')")
    public CommonResult<PageResult<NotifyMessageRespVO>> getNotifyMessagePage(@Valid NotifyMessagePageReqVO pageVO) {
        PageResult<NotifyMessageDO> pageResult = notifyMessageService.getNotifyMessagePage(pageVO);
        return success(NotifyMessageConvert.INSTANCE.convertPage(pageResult));
    }

}
