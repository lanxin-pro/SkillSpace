package cn.iocoder.educate.module.infra.service.codegen;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.CodegenTablePageReqVO;
import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenTableDO;

/**
 * 代码生成 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/7/23 11:06
 */
public interface CodegenService {

    /**
     * 获得表定义分页
     *
     * @param pageReqVO 分页条件
     * @return 表定义分页
     */
    PageResult<CodegenTableDO> getCodegenTablePage(CodegenTablePageReqVO pageReqVO);

}
