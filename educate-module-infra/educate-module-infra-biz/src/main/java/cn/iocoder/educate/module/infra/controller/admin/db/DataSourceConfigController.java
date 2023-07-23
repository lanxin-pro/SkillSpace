package cn.iocoder.educate.module.infra.controller.admin.db;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.module.infra.controller.admin.db.vo.DataSourceConfigRespVO;
import cn.iocoder.educate.module.infra.covert.db.DataSourceConfigConvert;
import cn.iocoder.educate.module.infra.dal.dataobject.db.DataSourceConfigDO;
import cn.iocoder.educate.module.infra.service.db.DataSourceConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/23 16:55
 */
@Tag(name = "管理后台 - 数据源配置")
@RestController
@RequestMapping("/infra/data-source-config")
@Validated
public class DataSourceConfigController {

    @Resource
    private DataSourceConfigService dataSourceConfigService;

    @GetMapping("/list")
    @Operation(summary = "获得数据源配置列表")
    public CommonResult<List<DataSourceConfigRespVO>> getDataSourceConfigList() {
        List<DataSourceConfigDO> list = dataSourceConfigService.getDataSourceConfigList();
        return success(DataSourceConfigConvert.INSTANCE.convertList(list));
    }


}
