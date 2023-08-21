package cn.iocoder.educate.module.system.controller.admin.sensitiveword;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.sensitiveword.vo.SensitiveWordCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.sensitiveword.vo.SensitiveWordPageReqVO;
import cn.iocoder.educate.module.system.controller.admin.sensitiveword.vo.SensitiveWordRespVO;
import cn.iocoder.educate.module.system.controller.admin.sensitiveword.vo.SensitiveWordUpdateReqVO;
import cn.iocoder.educate.module.system.convert.sensitiveword.SensitiveWordConvert;
import cn.iocoder.educate.module.system.dal.dataobject.sensitiveword.SensitiveWordDO;
import cn.iocoder.educate.module.system.service.sensitiveword.SensitiveWordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/21 8:03
 */
@Tag(name = "管理后台 - 敏感词")
@RestController
@RequestMapping("/system/sensitive-word")
@Validated
public class SensitiveWordController {

    @Resource
    private SensitiveWordService sensitiveWordService;

    @PostMapping("/create")
    @Operation(summary = "创建敏感词")
    @PreAuthorize("@lanxin.hasPermission('system:sensitive-word:create')")
    public CommonResult<Long> createSensitiveWord(@Valid @RequestBody SensitiveWordCreateReqVO createReqVO) {
        Long sensitiveWord = sensitiveWordService.createSensitiveWord(createReqVO);
        return success(sensitiveWord);
    }

    @PostMapping("/createBatch")
    @Operation(summary = "批量创建敏感词")
    @PreAuthorize("@lanxin.hasPermission('system:sensitive-word:createBatch')")
    public CommonResult<Long> createBatchSensitiveWord(@Valid @RequestBody SensitiveWordCreateReqVO createReqVO) {
        Long sensitiveWord = sensitiveWordService.createBatchSensitiveWord(createReqVO);
        return success(sensitiveWord);
    }

    @PutMapping("/update")
    @Operation(summary = "更新敏感词")
    @PreAuthorize("@lanxin.hasPermission('system:sensitive-word:update')")
    public CommonResult<Boolean> updateSensitiveWord(@Valid @RequestBody SensitiveWordUpdateReqVO updateReqVO) {
        sensitiveWordService.updateSensitiveWord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除敏感词")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@lanxin.hasPermission('system:sensitive-word:delete')")
    public CommonResult<Boolean> deleteSensitiveWord(@RequestParam("id") Long id) {
        sensitiveWordService.deleteSensitiveWord(id);
        return success(true);
    }

    @DeleteMapping("/deleteBatchIds")
    @Operation(summary = "批量删除敏感词")
    @Parameter(name = "ids", description = "编号", required = true)
    @PreAuthorize("@lanxin.hasPermission('system:sensitive-word:deleteBatch')")
    public CommonResult<Boolean> deleteBatchIdsSensitiveWord(@RequestBody String batchIds) {
        sensitiveWordService.deleteBatchSensitiveWord(batchIds);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得敏感词")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@lanxin.hasPermission('system:sensitive-word:query')")
    public CommonResult<SensitiveWordRespVO> getSensitiveWord(@RequestParam("id") Long id) {
        SensitiveWordDO sensitiveWord = sensitiveWordService.getSensitiveWord(id);
        return success(SensitiveWordConvert.INSTANCE.convert(sensitiveWord));
    }

    @GetMapping("/page")
    @Operation(summary = "获得敏感词分页")
    @PreAuthorize("@lanxin.hasPermission('system:sensitive-word:query')")
    public CommonResult<PageResult<SensitiveWordRespVO>> getSensitiveWordPage(@Valid SensitiveWordPageReqVO pageVO) {
        PageResult<SensitiveWordDO> pageResult = sensitiveWordService.getSensitiveWordPage(pageVO);
        return success(SensitiveWordConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/get-tags")
    @Operation(summary = "获取所有敏感词的标签数组")
    @PreAuthorize("@lanxin.hasPermission('system:sensitive-word:query')")
    public CommonResult<Set<String>> getSensitiveWordTagSet() {
        Set<String> sensitiveWordTagSet = sensitiveWordService.getSensitiveWordTagSet();
        return success(sensitiveWordTagSet);
    }

    @GetMapping("/validate-text")
    @Operation(summary = "获得文本所包含的不合法的敏感词数组")
    public CommonResult<List<String>> validateText(@RequestParam("text") String text,
                                                   @RequestParam(value = "tags", required = false) List<String> tags) {
        return success(sensitiveWordService.validateText(text, tags));
    }

}
