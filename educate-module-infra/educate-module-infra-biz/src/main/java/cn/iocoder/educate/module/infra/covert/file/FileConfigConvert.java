package cn.iocoder.educate.module.infra.covert.file;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.controller.admin.file.vo.config.FileConfigRespVO;
import cn.iocoder.educate.module.infra.dal.dataobject.file.FileConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 文件配置 Convert
 *
 * @Author: j-sentinel
 * @Date: 2023/8/2 16:43
 */
@Mapper
public interface FileConfigConvert {

    FileConfigConvert INSTANCE = Mappers.getMapper(FileConfigConvert.class);

    /**
     * 在进行PageResult<FileConfigRespVO> convertPage(PageResult<FileConfigDO> page); 转换的时候，这个是必须的参数
     *
     * @param bean
     * @return
     */
    FileConfigRespVO convert(FileConfigDO bean);

    PageResult<FileConfigRespVO> convertPage(PageResult<FileConfigDO> page);

}
