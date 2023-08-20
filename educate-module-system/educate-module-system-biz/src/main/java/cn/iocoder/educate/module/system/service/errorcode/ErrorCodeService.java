package cn.iocoder.educate.module.system.service.errorcode;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.errorcode.vo.ErrorCodeCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.errorcode.vo.ErrorCodePageReqVO;
import cn.iocoder.educate.module.system.controller.admin.errorcode.vo.ErrorCodeUpdateReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.errorcode.ErrorCodeDO;

import javax.validation.Valid;

/**
 * 错误码 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/8/20 10:38
 */
public interface ErrorCodeService {

    /**
     * 创建错误码
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createErrorCode(@Valid ErrorCodeCreateReqVO createReqVO);

    /**
     * 更新错误码
     *
     * @param updateReqVO 更新信息
     */
    void updateErrorCode(@Valid ErrorCodeUpdateReqVO updateReqVO);

    /**
     * 删除错误码
     *
     * @param id 编号
     */
    void deleteErrorCode(Long id);

    /**
     * 获得错误码
     *
     * @param id 编号
     * @return 错误码
     */
    ErrorCodeDO getErrorCode(Long id);

    /**
     * 获得错误码分页
     *
     * @param pageReqVO 分页查询
     * @return 错误码分页
     */
    PageResult<ErrorCodeDO> getErrorCodePage(ErrorCodePageReqVO pageReqVO);

}
