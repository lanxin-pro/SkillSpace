package cn.iocoder.educate.module.infra.dal.mysql.codegen;

import cn.iocoder.educate.module.infra.dal.dataobject.codegen.CodegenColumnDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/24 17:40
 */
@Mapper
public interface CodegenColumnMapper extends BaseMapper<CodegenColumnDO> {

    default void insertBatch(List<CodegenColumnDO> columns){
        Db.saveBatch(columns);
    }

}
