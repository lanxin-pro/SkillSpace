package cn.iocoder.educate.module.video.service.category;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.video.controller.admin.category.vo.tag.VideoTagReqVo;
import cn.iocoder.educate.module.video.controller.admin.category.vo.tag.VideoTagRespVO;
import cn.iocoder.educate.module.video.dal.dataobject.category.VideoTagDO;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/22 18:55
 */
public interface VideoTagService {

    PageResult<VideoTagDO> findVideoPanelsPage(VideoTagReqVo videoTagListReq);

}
