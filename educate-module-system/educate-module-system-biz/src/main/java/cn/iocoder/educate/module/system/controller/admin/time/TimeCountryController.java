package cn.iocoder.educate.module.system.controller.admin.time;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.module.system.controller.admin.time.vo.TimeCountryTimeZoneVO;
import cn.iocoder.educate.module.system.service.time.TimeCountryBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 国家地区
 *
 * @Author: j-sentinel
 * @Date: 2023/7/26 16:28
 */
@Slf4j
@Tag(name = "系统管理-国家地区")
@RestController
@RequestMapping("/system/country")
@Validated
public class TimeCountryController {

    @Resource
    private TimeCountryBusiness timeCountryBusiness;

    @GetMapping("/getCountryWithTimeZoneList")
    @Operation(summary = "获取所有国家和时区列表")
    public CommonResult<List<TimeCountryTimeZoneVO>> getCountryWithTimeZoneList() {
        return CommonResult.success(timeCountryBusiness.getCountryWithTimeZoneList());
    }


}
