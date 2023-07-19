package cn.iocoder.educate.module.system.controller.admin.dict;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.module.system.controller.admin.dict.vo.data.DictDataSimpleRespVO;
import cn.iocoder.educate.module.system.convert.dict.DictDataConvert;
import cn.iocoder.educate.module.system.dal.dataobject.dict.DictDataDO;
import cn.iocoder.educate.module.system.service.dict.DictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/19 15:09
 */
@Tag(name = "管理后台 - 字典数据")
@RestController
@RequestMapping("/system/dict-data")
@Validated
public class DictDataController {

    @Resource
    private DictDataService dictDataService;

    @GetMapping("/list-all-simple")
    @Operation(summary = "获得全部字典数据列表", description = "一般用于管理后台缓存字典数据在本地")
    // 无需添加权限认证，因为前端全局都需要
    public CommonResult<List<DictDataSimpleRespVO>> getSimpleDictDataList() {
        List<DictDataDO> list = dictDataService.getDictDataList();
        return CommonResult.success(DictDataConvert.INSTANCE.convertList(list));
    }



}
