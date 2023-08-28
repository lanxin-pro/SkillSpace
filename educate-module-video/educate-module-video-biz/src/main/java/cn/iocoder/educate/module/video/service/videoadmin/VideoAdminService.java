package cn.iocoder.educate.module.video.service.videoadmin;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminPageReqVO;
import cn.iocoder.educate.module.video.dal.dataobject.VideoAdminDO;

/**
 * 视频列表service接口
 *
 * @Author: j-sentinel
 * @Date: 2023/8/27 13:51
 */
public interface VideoAdminService {

    /**
     * 分页查询视频列表
     *
     * @param videoPageReqVO 视频详情分页
     * @return 数据库分页实体
     */
    PageResult<VideoAdminDO> getVideoAdminPage(VideoAdminPageReqVO videoPageReqVO);

}
