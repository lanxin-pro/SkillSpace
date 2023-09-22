package cn.iocoder.educate.module.video.service.category;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.video.controller.admin.category.vo.tag.VideoTagReqVo;
import cn.iocoder.educate.module.video.controller.admin.category.vo.tag.VideoTagRespVO;
import cn.iocoder.educate.module.video.dal.dataobject.category.VideoTagDO;
import cn.iocoder.educate.module.video.dal.mysql.category.VideoTagMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/22 18:55
 */
@Service
@Slf4j
public class VideoTagServiceImpl implements VideoTagService {

    @Resource
    private VideoTagMapper videoTagMapper;

    @Override
    public PageResult<VideoTagDO> findVideoPanelsPage(VideoTagReqVo videoTagListReq) {
        return videoTagMapper.selectPage(videoTagListReq);
    }

}
