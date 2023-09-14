package cn.iocoder.educate.module.mp.controller.admin.material;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.material.vo.MpMaterialPageReqVO;
import cn.iocoder.educate.module.mp.controller.admin.material.vo.MpMaterialRespVO;
import cn.iocoder.educate.module.mp.controller.admin.material.vo.MpMaterialUploadPermanentReqVO;
import cn.iocoder.educate.module.mp.controller.admin.material.vo.MpMaterialUploadRespVO;
import cn.iocoder.educate.module.mp.convert.material.MpMaterialConvert;
import cn.iocoder.educate.module.mp.dal.dataobject.material.MpMaterialDO;
import cn.iocoder.educate.module.mp.service.material.MpMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.io.IOException;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/11 14:52
 */
@Tag(name = "管理后台 - 公众号素材")
@RestController
@RequestMapping("/mp/material")
@Validated
public class MpMaterialController {

    @Resource
    private MpMaterialService mpMaterialService;

    @Operation(summary = "获得素材分页")
    @GetMapping("/page")
    @PreAuthorize("@lanxin.hasPermission('mp:material:query')")
    public CommonResult<PageResult<MpMaterialRespVO>> getMaterialPage(@Valid MpMaterialPageReqVO pageReqVO) {
        PageResult<MpMaterialDO> pageResult = mpMaterialService.getMaterialPage(pageReqVO);
        return success(MpMaterialConvert.INSTANCE.convertPage(pageResult));
    }

    @Operation(summary = "删除素材")
    @DeleteMapping("/delete-permanent")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@lanxin.hasPermission('mp:material:delete')")
    public CommonResult<Boolean> deleteMaterial(@RequestParam("id") Long id) {
        mpMaterialService.deleteMaterial(id);
        return success(true);
    }

    @Operation(summary = "上传永久素材")
    @PostMapping("/upload-permanent")
    @PreAuthorize("@lanxin.hasPermission('mp:material:upload-permanent')")
    public CommonResult<MpMaterialUploadRespVO> uploadPermanentMaterial(
            @Valid MpMaterialUploadPermanentReqVO reqVO) throws IOException {
        MpMaterialDO material = mpMaterialService.uploadPermanentMaterial(reqVO);
        return success(MpMaterialConvert.INSTANCE.convert(material));
    }

}
