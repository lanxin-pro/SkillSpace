package cn.iocoder.educate.module.system.controller.admin.system;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.educate.module.system.controller.admin.system.vo.SystemDateTimeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/26 18:07
 */
@Slf4j
@Tag(name = "系统管理-系统时间")
@RestController
@RequestMapping("/systemTime")
@Validated
public class SystemController {

    @GetMapping("/getServerTime")
    @Operation(summary = "获取服务器时间")
    public CommonResult<SystemDateTimeVO> getServerTime() {
        SystemDateTimeVO systemDateTimeVO = new SystemDateTimeVO();
        systemDateTimeVO.setTime(System.currentTimeMillis());
        return CommonResult.success(systemDateTimeVO);
    }


}
