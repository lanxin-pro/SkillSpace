package cn.iocoder.educate.module.system.api.errorcode;

import cn.iocoder.educate.module.system.api.errorcode.dto.ErrorCodeAutoGenerateReqDTO;
import cn.iocoder.educate.module.system.service.errorcode.ErrorCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 错误码 Api 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/8/20 13:07
 */
@Slf4j
@Service
public class ErrorCodeApiImpl implements ErrorCodeApi {

    @Resource
    private ErrorCodeService errorCodeService;

    @Override
    public void autoGenerateErrorCodeList(List<ErrorCodeAutoGenerateReqDTO> autoGenerateDTOs) {
        errorCodeService.autoGenerateErrorCodes(autoGenerateDTOs);
    }

}
