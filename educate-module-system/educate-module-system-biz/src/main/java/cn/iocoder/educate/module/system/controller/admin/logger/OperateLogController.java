package cn.iocoder.educate.module.system.controller.admin.logger;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.MapUtils;
import cn.iocoder.educate.module.system.controller.admin.logger.vo.OperateLogPageReqVO;
import cn.iocoder.educate.module.system.controller.admin.logger.vo.OperateLogRespVO;
import cn.iocoder.educate.module.system.convert.logger.OperateLogConvert;
import cn.iocoder.educate.module.system.dal.dataobject.logger.OperateLogDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.service.logger.OperateLogService;
import cn.iocoder.educate.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.educate.framework.common.pojo.CommonResult.success;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/6 17:11
 */
@Tag(name = "管理后台 - 操作日志")
@RestController
@RequestMapping("/system/operate-log")
@Validated
public class OperateLogController {

    @Resource
    private OperateLogService operateLogService;

    @Resource
    private AdminUserService adminUserService;

    @GetMapping("/page")
    @Operation(summary = "查看操作日志分页列表")
    public CommonResult<PageResult<OperateLogRespVO>> pageOperateLog(@Valid OperateLogPageReqVO reqVO) {
        PageResult<OperateLogDO> pageResult = operateLogService.getOperateLogPage(reqVO);

        // 获得拼接需要的数据
        List<Long> userIds = pageResult.getList()
                .stream()
                .map(OperateLogDO::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        // 因为map的key是不可重复的，所以重复的用户就覆盖省略
        Map<Long, AdminUserDO> userMap = adminUserService.getUserMap(userIds);
        // 拼接数据
        List<OperateLogRespVO> list = new ArrayList<>(pageResult.getList().size());
        pageResult.getList().forEach(operateLogDO -> {
            OperateLogRespVO respVO = OperateLogConvert.INSTANCE.convert(operateLogDO);
            list.add(respVO);
            // 拼接用户信息
            MapUtils.findAndThen(userMap, operateLogDO.getUserId(), user -> respVO.setUserNickname(user.getNickname()));
        });

        return success(new PageResult<>(list, pageResult.getTotal()));
    }
}
