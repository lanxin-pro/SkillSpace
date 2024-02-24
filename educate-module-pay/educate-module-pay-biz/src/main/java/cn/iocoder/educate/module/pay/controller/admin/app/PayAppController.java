package cn.iocoder.educate.module.pay.controller.admin.app;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.module.pay.controller.admin.app.vo.PayAppPageItemRespVO;
import cn.iocoder.educate.module.pay.controller.admin.app.vo.PayAppPageReqVO;
import cn.iocoder.educate.module.pay.convert.app.PayAppConvert;
import cn.iocoder.educate.module.pay.dal.dataobject.app.PayAppDO;
import cn.iocoder.educate.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.educate.module.pay.service.app.PayAppService;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.pay.service.channel.PayChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author j-sentinel
 * @date 2024/2/22 11:11
 */
@Slf4j
@Tag(name = "管理后台 - 支付应用信息")
@RestController
@RequestMapping("/pay/app")
@Validated
public class PayAppController {

    @Resource
    private PayAppService appService;

    @Resource
    private PayChannelService channelService;

    @GetMapping("/page")
    @Operation(summary = "获得支付应用信息分页")
    @PreAuthorize("@lanxin.hasPermission('pay:app:query')")
    public CommonResult<PageResult<PayAppPageItemRespVO>> getAppPage(@Valid PayAppPageReqVO pageVO) {
        // 得到应用分页列表
        PageResult<PayAppDO> pageResult = appService.getAppPage(pageVO);
        if(CollUtil.isEmpty(pageResult.getList())) {
            return CommonResult.success(PageResult.empty());
        }
        // 这里还需要 channelCodes 支付渠道编码
        Set<Long> appIds = pageResult.getList().stream()
                .map(PayAppDO::getId).collect(Collectors.toSet());
        List<PayChannelDO> channels = channelService.getChannelListByAppIds(appIds);
        return CommonResult.success(PayAppConvert.INSTANCE.convertPage(pageResult, channels));
    }


}
