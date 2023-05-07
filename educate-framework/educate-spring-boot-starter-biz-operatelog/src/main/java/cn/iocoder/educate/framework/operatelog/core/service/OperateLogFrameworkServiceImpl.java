package cn.iocoder.educate.framework.operatelog.core.service;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.educate.module.system.api.logger.OperateLogApi;
import cn.iocoder.educate.module.system.api.logger.dto.OperateLogCreateReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 14:56
 */
@RequiredArgsConstructor
public class OperateLogFrameworkServiceImpl implements OperateLogFrameworkService{

    private final OperateLogApi operateLogApi;

    @Override
    @Async
    public void createOperateLog(OperateLog operateLog) {
        OperateLogCreateReqDTO reqDTO = BeanUtil.copyProperties(operateLog, OperateLogCreateReqDTO.class);
        operateLogApi.createOperateLog(reqDTO);
    }
}
