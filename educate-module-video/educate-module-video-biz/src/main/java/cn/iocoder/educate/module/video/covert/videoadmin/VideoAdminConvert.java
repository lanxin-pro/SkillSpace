package cn.iocoder.educate.module.video.covert.videoadmin;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminCreateReqVO;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminRespVo;
import cn.iocoder.educate.module.video.dal.dataobject.VideoAdminDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/27 13:50
 */
@Mapper
public interface VideoAdminConvert {

    VideoAdminConvert INSTANCE = Mappers.getMapper(VideoAdminConvert.class);

    VideoAdminRespVo convert(VideoAdminDO videoAdminDO);

    VideoAdminDO convert(VideoAdminCreateReqVO videoAdminReqVO);

    PageResult<VideoAdminRespVo> convertPage(PageResult<VideoAdminDO> pageResult);

}
