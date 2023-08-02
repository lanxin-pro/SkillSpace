package cn.iocoder.educate.module.infra.covert.file;

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

}
