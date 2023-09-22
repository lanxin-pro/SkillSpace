package cn.iocoder.educate.module.video.covert.category;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.video.controller.admin.category.vo.category.VideoCategoryRespVO;
import cn.iocoder.educate.module.video.controller.admin.category.vo.tag.VideoTagRespVO;
import cn.iocoder.educate.module.video.dal.dataobject.category.VideoCategoryDO;
import cn.iocoder.educate.module.video.dal.dataobject.category.VideoTagDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/22 12:36
 */
@Mapper
public interface VideoTagConvert {

    VideoTagConvert INSTANCE = Mappers.getMapper(VideoTagConvert.class);

    VideoTagRespVO convert(VideoTagDO bean);

    PageResult<VideoTagRespVO> convertPage(PageResult<VideoTagDO> page);

}
