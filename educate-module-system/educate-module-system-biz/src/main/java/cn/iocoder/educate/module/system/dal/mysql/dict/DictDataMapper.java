package cn.iocoder.educate.module.system.dal.mysql.dict;

import cn.iocoder.educate.module.system.dal.dataobject.dict.DictDataDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/19 15:12
 */
@Mapper
public interface DictDataMapper extends BaseMapper<DictDataDO> {

    default List<DictDataDO> selectList(){
        return this.selectList(new LambdaQueryWrapper<>());
    }
}
