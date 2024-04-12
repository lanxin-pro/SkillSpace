package cn.iocoder.educate.module.pay.controller.admin.demo;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageParam;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.educate.module.pay.controller.admin.demo.vo.transfer.PayDemoTransferCreateReqVO;
import cn.iocoder.educate.module.pay.controller.admin.demo.vo.transfer.PayDemoTransferRespVO;
import cn.iocoder.educate.module.pay.convert.demo.PayDemoTransferConvert;
import cn.iocoder.educate.module.pay.dal.dataobject.demo.PayDemoTransferDO;
import cn.iocoder.educate.module.pay.service.demo.PayDemoTransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @author j-sentinel
 * @date 2024/4/5 15:03
 */
@Tag(name = "管理后台 - 示例转账单")
@RestController
@RequestMapping("/pay/demo-transfer")
@Validated
public class PayDemoTransferController {

    @Resource
    private PayDemoTransferService demoTransferService;

/*    @PostMapping("/create")
    @Operation(summary = "创建示例转账订单")
    public CommonResult<Long> createDemoTransfer(@Valid @RequestBody PayDemoTransferCreateReqVO createReqVO) {
        return success(demoTransferService.createDemoTransfer(createReqVO));
    }

    @GetMapping("/page")
    @Operation(summary = "获得示例转账订单分页")
    public CommonResult<PageResult<PayDemoTransferRespVO>> getDemoTransferPage(@Valid PageParam pageVO) {
        PageResult<PayDemoTransferDO> pageResult = demoTransferService.getDemoTransferPage(pageVO);
        return success(PayDemoTransferConvert.INSTANCE.convertPage(pageResult));
    }*/

}
