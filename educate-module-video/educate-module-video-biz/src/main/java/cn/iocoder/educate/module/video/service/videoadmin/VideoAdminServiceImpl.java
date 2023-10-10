package cn.iocoder.educate.module.video.service.videoadmin;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminCreateReqVO;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminPageReqVO;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminUpdateReqVO;
import cn.iocoder.educate.module.video.covert.videoadmin.VideoAdminConvert;
import cn.iocoder.educate.module.video.dal.dataobject.VideoAdminDO;
import cn.iocoder.educate.module.video.dal.mysql.VideoAdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDateTime;

import static cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil.exception;

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

    @Override
    public Long createVideo(VideoAdminCreateReqVO videoAdminReqVO) {
        // validateVideoAdminForCreateOrUpdate(videoAdminReqVO);
        VideoAdminDO videoAdminDO = VideoAdminConvert.INSTANCE.convert(videoAdminReqVO);
        // 如果点击了开启状态,就更新启动时间
        if(videoAdminReqVO.getEnableStatus().equals(CommonStatusEnum.ENABLE)){
            videoAdminDO.setOpenTime(LocalDateTime.now());
        }
        videoAdminMapper.insert(videoAdminDO);
        return videoAdminDO.getId();
    }

    @Override
    public void updateVideo(VideoAdminUpdateReqVO videoAdminReqVO) {
        VideoAdminDO videoAdminDO = VideoAdminConvert.INSTANCE.convert(videoAdminReqVO);
        videoAdminMapper.updateById(videoAdminDO);
    }

    @Override
    public VideoAdminDO getFileVideo(Long id) {
        return videoAdminMapper.selectById(id);
    }

}
