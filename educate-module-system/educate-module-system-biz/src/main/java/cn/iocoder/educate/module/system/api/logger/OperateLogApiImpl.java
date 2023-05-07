package cn.iocoder.educate.module.system.api.logger;

import cn.iocoder.educate.module.system.api.logger.dto.OperateLogCreateReqDTO;
import cn.iocoder.educate.module.system.service.logger.OperateLogService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 15:02
 * 操作日志 API 实现类
 */
@Service
@Validated
public class OperateLogApiImpl implements OperateLogApi{

    @Resource
    private OperateLogService operateLogService;

    @Override
    public void createOperateLog(OperateLogCreateReqDTO createReqDTO) {
        operateLogService.createOperateLog(createReqDTO);
    }
}
