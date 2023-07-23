package cn.iocoder.educate.module.infra.service.codegen;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.CodegenTablePageReqVO;
import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenTableDO;
import cn.iocoder.educate.module.infra.dal.mysql.codegen.CodegenTableMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/23 11:06
 */
@Slf4j
@Service
public class CodegenServiceImpl implements CodegenService {

    @Resource
    private CodegenTableMapper codegenTableMapper;

    @Override
    public PageResult<CodegenTableDO> getCodegenTablePage(CodegenTablePageReqVO pageReqVO) {
        return codegenTableMapper.selectPage(pageReqVO);
    }

}
