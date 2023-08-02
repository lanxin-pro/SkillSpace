package cn.iocoder.educate.module.infra.covert.file;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.infra.controller.admin.file.vo.file.FileRespVO;
import cn.iocoder.educate.module.infra.dal.dataobject.file.FileDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/2 16:42
 */
@Mapper
public interface FileConvert {

    FileConvert INSTANCE = Mappers.getMapper(FileConvert.class);

    PageResult<FileRespVO> convertPage(PageResult<FileDO> page);

}
