package cn.iocoder.educate.module.system.service.errorcode;

import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.errorcode.vo.ErrorCodeCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.errorcode.vo.ErrorCodePageReqVO;
import cn.iocoder.educate.module.system.controller.admin.errorcode.vo.ErrorCodeUpdateReqVO;
import cn.iocoder.educate.module.system.convert.errorcode.ErrorCodeConvert;
import cn.iocoder.educate.module.system.dal.dataobject.errorcode.ErrorCodeDO;
import cn.iocoder.educate.module.system.dal.mysql.errorcode.ErrorCodeMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.enums.errorcode.ErrorCodeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 错误码 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/8/20 10:40
 */
@Service
@Validated
@Slf4j
public class ErrorCodeServiceImpl implements ErrorCodeService {

    @Resource
    private ErrorCodeMapper errorCodeMapper;

    @Override
    public Long createErrorCode(ErrorCodeCreateReqVO createReqVO) {
        // 校验 code 重复
        validateCodeDuplicate(createReqVO.getCode(), null);

        // 插入
        ErrorCodeDO errorCode = ErrorCodeConvert.INSTANCE.convert(createReqVO)
                .setType(ErrorCodeTypeEnum.MANUAL_OPERATION.getType());
        errorCodeMapper.insert(errorCode);
        // 返回
        return errorCode.getId();
    }

    @Override
    public void updateErrorCode(ErrorCodeUpdateReqVO errorCodeUpdateReqVO) {
        // 校验存在
        validateErrorCodeExists(errorCodeUpdateReqVO.getId());
        // 校验 code 重复
        validateCodeDuplicate(errorCodeUpdateReqVO.getCode(), errorCodeUpdateReqVO.getId());

        // 更新
        ErrorCodeDO errorCodeDO = ErrorCodeConvert.INSTANCE.convert(errorCodeUpdateReqVO)
                .setType(ErrorCodeTypeEnum.MANUAL_OPERATION.getType());
        errorCodeMapper.updateById(errorCodeDO);
    }

    @Override
    public void deleteErrorCode(Long id) {
        // 校验存在
        validateErrorCodeExists(id);
        // 删除
        errorCodeMapper.deleteById(id);
    }

    @Override
    public ErrorCodeDO getErrorCode(Long id) {
        return errorCodeMapper.selectById(id);
    }

    @Override
    public PageResult<ErrorCodeDO> getErrorCodePage(ErrorCodePageReqVO errorCodePageReqVO) {
        return errorCodeMapper.selectPage(errorCodePageReqVO);
    }

    /**
     * 校验错误码的唯一字段是否重复
     *
     * 是否存在相同编码的错误码
     *
     * @param code 错误码编码
     * @param id 错误码编号
     */
    public void validateCodeDuplicate(Integer code, Long id) {
        ErrorCodeDO errorCodeDO = errorCodeMapper.selectByCode(code);
        if (errorCodeDO == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的错误码
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ERROR_CODE_DUPLICATE);
        }
        if (!errorCodeDO.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ERROR_CODE_DUPLICATE);
        }
    }

    public void validateErrorCodeExists(Long id) {
        if (errorCodeMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ERROR_CODE_NOT_EXISTS);
        }
    }

}
