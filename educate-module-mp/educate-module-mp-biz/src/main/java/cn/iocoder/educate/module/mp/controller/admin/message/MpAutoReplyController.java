package cn.iocoder.educate.module.mp.controller.admin.message;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.autoreply.MpAutoReplyCreateReqVO;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.autoreply.MpAutoReplyRespVO;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.autoreply.MpAutoReplyUpdateReqVO;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.autoreply.MpMessagePageReqVO;
import cn.iocoder.educate.module.mp.convert.message.MpAutoReplyConvert;
import cn.iocoder.educate.module.mp.dal.dataobject.message.MpAutoReplyDO;
import cn.iocoder.educate.module.mp.service.message.MpAutoReplyService;
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
 * @Date: 2023/9/1 20:22
 */
@Tag(name = "管理后台 - 公众号自动回复")
@RestController
@RequestMapping("/mp/auto-reply")
@Validated
public class MpAutoReplyController {

    @Resource
    private MpAutoReplyService mpAutoReplyService;

    @GetMapping("/page")
    @Operation(summary = "获得公众号自动回复分页")
    @PreAuthorize("@lanxin.hasPermission('mp:auto-reply:query')")
    public CommonResult<PageResult<MpAutoReplyRespVO>> getAutoReplyPage(@Valid MpMessagePageReqVO pageVO) {
        PageResult<MpAutoReplyDO> pageResult = mpAutoReplyService.getAutoReplyPage(pageVO);
        return success(MpAutoReplyConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/get")
    @Operation(summary = "获得公众号自动回复")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@lanxin.hasPermission('mp:auto-reply:query')")
    public CommonResult<MpAutoReplyRespVO> getAutoReply(@RequestParam("id") Long id) {
        MpAutoReplyDO autoReply = mpAutoReplyService.getAutoReply(id);
        return success(MpAutoReplyConvert.INSTANCE.convert(autoReply));
    }

    @PostMapping("/create")
    @Operation(summary = "创建公众号自动回复")
    @PreAuthorize("@lanxin.hasPermission('mp:auto-reply:create')")
    public CommonResult<Long> createAutoReply(@Valid @RequestBody MpAutoReplyCreateReqVO createReqVO) {
        return success(mpAutoReplyService.createAutoReply(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新公众号自动回复")
    @PreAuthorize("@lanxin.hasPermission('mp:auto-reply:update')")
    public CommonResult<Boolean> updateAutoReply(@Valid @RequestBody MpAutoReplyUpdateReqVO updateReqVO) {
        mpAutoReplyService.updateAutoReply(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除公众号自动回复")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@lanxin.hasPermission('mp:auto-reply:delete')")
    public CommonResult<Boolean> deleteAutoReply(@RequestParam("id") Long id) {
        mpAutoReplyService.deleteAutoReply(id);
        return success(true);
    }

}
