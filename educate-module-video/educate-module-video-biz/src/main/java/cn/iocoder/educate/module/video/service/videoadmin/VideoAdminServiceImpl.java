package cn.iocoder.educate.module.video.service.videoadmin;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminPageReqVO;
import cn.iocoder.educate.module.video.dal.dataobject.VideoAdminDO;
import cn.iocoder.educate.module.video.dal.mysql.VideoAdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 视频列表service实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/8/27 13:51
 */
@Slf4j
@Service
public class VideoAdminServiceImpl implements VideoAdminService {

    @Resource
    private VideoAdminMapper videoAdminMapper;

    @Override
    public PageResult<VideoAdminDO> getVideoAdminPage(VideoAdminPageReqVO videoPageReqVO) {
        return videoAdminMapper.selectPage(videoPageReqVO);
    }

}
