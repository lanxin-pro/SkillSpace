package cn.iocoder.educate.module.infra.dal.mysql.file;

import cn.iocoder.educate.module.infra.dal.dataobject.file.FileDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/13 11:35
 */
@Mapper
public interface FileMapper extends BaseMapper<FileDO> {
}
