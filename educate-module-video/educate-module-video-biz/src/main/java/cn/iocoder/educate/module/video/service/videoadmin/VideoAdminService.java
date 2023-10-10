package cn.iocoder.educate.module.video.service.videoadmin;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminCreateReqVO;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminPageReqVO;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminUpdateReqVO;
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

    /**
     * 创建视频
     *
     * @param liveStudioSourceManegeVO 视频craeteVO
     * @return
     */
    Long createVideo(VideoAdminCreateReqVO liveStudioSourceManegeVO);

    /**
     * 修改视频
     *
     * @param liveStudioSourceManegeVO 修改视频updateVO
     */
    void updateVideo(VideoAdminUpdateReqVO liveStudioSourceManegeVO);

    /**
     * 获取视频信息
     *
     * @param id 主键
     * @return 视频信息
     */
    VideoAdminDO getFileVideo(Long id);
}
