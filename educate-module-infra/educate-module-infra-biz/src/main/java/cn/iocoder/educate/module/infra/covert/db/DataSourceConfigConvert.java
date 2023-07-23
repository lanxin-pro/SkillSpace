package cn.iocoder.educate.module.infra.covert.db;

import cn.iocoder.educate.module.infra.controller.admin.db.vo.DataSourceConfigRespVO;
import cn.iocoder.educate.module.infra.dal.dataobject.db.DataSourceConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * 数据源配置 Convert
 *
 * @Author: j-sentinel
 * @Date: 2023/7/23 17:13
 */
@Mapper
public interface DataSourceConfigConvert {

    DataSourceConfigConvert INSTANCE = Mappers.getMapper(DataSourceConfigConvert.class);

    List<DataSourceConfigRespVO> convertList(List<DataSourceConfigDO> list);

}
