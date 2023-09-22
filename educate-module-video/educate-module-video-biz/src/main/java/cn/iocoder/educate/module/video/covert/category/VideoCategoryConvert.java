package cn.iocoder.educate.module.video.covert.category;

import cn.iocoder.educate.module.video.controller.admin.category.vo.VideoCategoryRespVO;
import cn.iocoder.educate.module.video.dal.dataobject.category.VideoCategoryDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/22 12:36
 */
@Mapper
public interface VideoCategoryConvert {

    VideoCategoryConvert INSTANCE = Mappers.getMapper(VideoCategoryConvert.class);

    VideoCategoryRespVO covert(VideoCategoryDO data);

    List<VideoCategoryRespVO> covertList(List<VideoCategoryDO> videoCategoryDOS);

}
