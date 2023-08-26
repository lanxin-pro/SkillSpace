package cn.iocoder.educate.module.infra.service.codegen;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenColumnDO;
import cn.iocoder.educate.module.infra.dal.mysql.codegen.CodegenColumnMapper;
import cn.iocoder.educate.module.infra.service.codegen.inner.CodegenEngine;
import cn.iocoder.educate.module.system.api.user.AdminUserApi;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.CodegenCreateListReqVO;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.table.CodegenTablePageReqVO;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.table.DatabaseTableRespVO;
import cn.iocoder.educate.module.infra.covert.codegen.CodegenConvert;
import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenTableDO;
import cn.iocoder.educate.module.infra.dal.mysql.codegen.CodegenTableMapper;
import cn.iocoder.educate.module.infra.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.infra.enums.codegen.CodegenSceneEnum;
import cn.iocoder.educate.module.infra.framwork.codegen.config.CodegenProperties;
import cn.iocoder.educate.module.infra.service.codegen.inner.CodegenBuilder;
import cn.iocoder.educate.module.infra.service.db.DatabaseTableService;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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

    @Resource
    private CodegenColumnMapper codegenColumnMapper;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private CodegenBuilder codegenBuilder;

    @Resource
    private CodegenProperties codegenProperties;

    @Resource
    private CodegenEngine codegenEngine;

    @Override
    public PageResult<CodegenTableDO> getCodegenTablePage(CodegenTablePageReqVO pageReqVO) {
        return codegenTableMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DatabaseTableRespVO> getDatabaseTableList(Long dataSourceConfigId, String name, String comment) {
        // 数据库全部表的信息，我查询全表的时候就应该筛选出来
        List<TableInfo> tables = databaseTableService.getTableList(dataSourceConfigId,name,comment);
        // 移除已经生成的表
        // 移除在 Codegen 中，这个是需要排除的，已经存在的
        List<CodegenTableDO> codegenTableDOS = codegenTableMapper.selectListByDataSourceConfigId(dataSourceConfigId);
        if(CollUtil.isEmpty(codegenTableDOS)){
            return new ArrayList<>();
        }
        // 需要排除的表
        Set<String> existsTables = codegenTableDOS
                .stream()
                .map(CodegenTableDO::getTableName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        // 移除
        tables.removeIf(tableInfo -> {
            // CodegenTableDO表中全部能够匹配的数据库表的name
            return existsTables.contains(tableInfo.getName());
        });
        return CodegenConvert.INSTANCE.convertList04(tables);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> createCodegenList(Long loginUserId, CodegenCreateListReqVO reqVO) {
        // 创建数组是为了能够返回添加的ids
        ArrayList<Long> ids = new ArrayList<>(reqVO.getTableNames().size());
        // 遍历添加。虽然效率会低一点，但是没必要做成完全批量，因为不会这么大量
        reqVO.getTableNames().forEach(tableName -> {
            Long codegen = createCodegen(loginUserId,
                    reqVO.getDataSourceConfigId(),
                    tableName);
            ids.add(codegen);
        });
        return ids;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCodegen(Long tableId) {
        // 校验是否已经存在
        if(codegenTableMapper.selectById(tableId) == null){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CODEGEN_TABLE_NOT_EXISTS);
        }
        // 删除 table 表定义
        codegenTableMapper.deleteById(tableId);
        // 删除 column 字段定义
        codegenColumnMapper.deleteListByTableId(tableId);
    }

    @Override
    public Map<String, String> generationCodes(Long tableId) {
        // 校验表是否已经存在
        CodegenTableDO codegenTableDO = codegenTableMapper.selectById(tableId);
        if(codegenTableDO == null){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CODEGEN_TABLE_NOT_EXISTS);
        }
        // 字段定义，这里已经在导入表的时候添加过了
        List<CodegenColumnDO> codegenColumnDO = codegenColumnMapper.selectListByTableId(tableId);
        if(CollUtil.isEmpty(codegenColumnDO)){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CODEGEN_COLUMN_NOT_EXISTS);
        }

        // 执行生成
        return codegenEngine.execute(codegenTableDO, codegenColumnDO);
    }

    public Long createCodegen(Long userId, Long dataSourceConfigId, String tableName) {
        // 从数据库中，获得数据库表结构，因为我需要更加详细的信息
        TableInfo tableInfo = databaseTableService.getTable(dataSourceConfigId, tableName);
        // 导入
        return createCodegen0(userId, dataSourceConfigId, tableInfo);
    }

    private Long createCodegen0(Long userId, Long dataSourceConfigId, TableInfo tableInfo) {
        // 校验导入的表和字段非空
        validateTableInfo(tableInfo);
        // 校验是否已经存在（源 + 表名不能重复）
        if(codegenTableMapper.selectByTableNameAndDataSourceConfigId(tableInfo.getName(),dataSourceConfigId) != null){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CODEGEN_TABLE_EXISTS);
        }
        // 构建 CodegenTableDO 对象，插入到 DB 中
        CodegenTableDO table = codegenBuilder.buildTable(tableInfo);
        table.setDataSourceConfigId(dataSourceConfigId);
        // 默认配置下，使用管理后台的模板
        table.setScene(CodegenSceneEnum.ADMIN.getScene());
        table.setFrontType(codegenProperties.getFrontType());
        table.setAuthor(adminUserApi.getUser(userId).getNickname());
        codegenTableMapper.insert(table);

        // 构建 CodegenColumnDO 数组，插入到 DB 中
        List<CodegenColumnDO> columns = codegenBuilder.buildColumns(table.getId(), tableInfo.getFields());
        // 如果没有主键，则使用第一个字段作为主键
        if (!tableInfo.isHavePrimaryKey()) {
            columns.get(0).setPrimaryKey(true);
        }
        codegenColumnMapper.insertBatch(columns);
        return table.getId();
    }

    private void validateTableInfo(TableInfo tableInfo) {
        // 表为空报错
        if (tableInfo == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CODEGEN_IMPORT_TABLE_NULL);
        }
        // 注释为空报错
        if (StrUtil.isEmpty(tableInfo.getComment())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CODEGEN_TABLE_INFO_TABLE_COMMENT_IS_NULL);
        }
        // 字段必须存在
        if (CollUtil.isEmpty(tableInfo.getFields())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CODEGEN_IMPORT_COLUMNS_NULL);
        }
        tableInfo.getFields().forEach(field -> {
            // 每一个字段都必须要有注释
            if (StrUtil.isEmpty(field.getComment())) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.CODEGEN_TABLE_INFO_COLUMN_COMMENT_IS_NULL,
                        field.getName());
            }
        });
    }

}
