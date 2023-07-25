package cn.iocoder.educate.module.infra.service.codegen;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.CodegenCreateListReqVO;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.table.CodegenTablePageReqVO;
import cn.iocoder.educate.module.infra.controller.admin.codegen.vo.table.DatabaseTableRespVO;
import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenTableDO;

import java.util.List;

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

    /**
     * 获得数据库自带的表定义列表
     *
     * @param dataSourceConfigId 数据源的配置编号
     * @param name 表名称
     * @param comment 表描述
     * @return 表定义列表
     */
    List<DatabaseTableRespVO> getDatabaseTableList(Long dataSourceConfigId, String name, String comment);

    /**
     * 基于数据库的表结构，创建代码生成器的表定义
     *
     * @param loginUserId 用户编号
     * @param reqVO 表信息
     * @return 创建的表定义的编号数组
     */
    List<Long> createCodegenList(Long loginUserId, CodegenCreateListReqVO reqVO);

    /**
     * 删除数据库的表和字段定义
     *
     * @param tableId 数据编号
     */
    void deleteCodegen(Long tableId);
}
