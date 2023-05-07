package cn.iocoder.educate.module.system.service.logger;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.module.system.api.logger.dto.OperateLogCreateReqDTO;
import cn.iocoder.educate.module.system.convert.logger.OperateLogConvert;
import cn.iocoder.educate.module.system.dal.dataobject.logger.OperateLogDO;
import cn.iocoder.educate.module.system.dal.mysql.logger.OperateLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 15:08
 */
@Service
@Validated
@Slf4j
public class OperateLogServiceImpl implements OperateLogService{

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public void createOperateLog(OperateLogCreateReqDTO createReqDTO) {
        OperateLogDO convertDo = OperateLogConvert.INSTANCE.convert(createReqDTO);
        // 限制字符串长度，如果超过指定长度，截取指定长度并在末尾加"..."
        convertDo.setJavaMethodArgs(StrUtil.maxLength(convertDo.getJavaMethodArgs(),
                OperateLogDO.JAVA_METHOD_ARGS_MAX_LENGTH));
        convertDo.setResultData(StrUtil.maxLength(convertDo.getResultData(),
                OperateLogDO.RESULT_MAX_LENGTH));
        operateLogMapper.insert(convertDo);
    }
}
