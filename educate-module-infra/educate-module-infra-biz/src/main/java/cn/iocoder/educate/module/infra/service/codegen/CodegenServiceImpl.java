package cn.iocoder.educate.module.infra.service.codegen;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.CodegenTablePageReqVO;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.DatabaseTableRespVO;
import cn.iocoder.educate.module.infra.covert.codegen.CodegenConvert;
import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenTableDO;
import cn.iocoder.educate.module.infra.dal.mysql.codegen.CodegenTableMapper;
import cn.iocoder.educate.module.infra.service.db.DatabaseTableService;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/23 11:06
 */
@Slf4j
@Service
public class CodegenServiceImpl implements CodegenService {

    @Resource
    private CodegenTableMapper codegenTableMapper;

    @Resource
    private DatabaseTableService databaseTableService;

    @Override
    public PageResult<CodegenTableDO> getCodegenTablePage(CodegenTablePageReqVO pageReqVO) {
        return codegenTableMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DatabaseTableRespVO> getDatabaseTableList(Long dataSourceConfigId, String name, String comment) {
        List<TableInfo> tables = databaseTableService.getTableList(dataSourceConfigId,name,comment);
        // 移除已经生成的表
        // 移除在 Codegen 中，已经存在的
        return  CodegenConvert.INSTANCE.convertList04(tables);
    }

}
