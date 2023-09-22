package cn.iocoder.educate.module.video.dal.mysql.category;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.video.controller.admin.category.vo.tag.VideoTagReqVo;
import cn.iocoder.educate.module.video.controller.admin.category.vo.tag.VideoTagRespVO;
import cn.iocoder.educate.module.video.dal.dataobject.category.VideoTagDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/22 18:56
 */
@Mapper
public interface VideoTagMapper extends BaseMapper<VideoTagDO> {

    default PageResult<VideoTagDO> selectPage(VideoTagReqVo videoTagListReq) {
        LambdaQueryWrapper<VideoTagDO> videoTagDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        videoTagDOLambdaQueryWrapper
                .like(StringUtils.hasText(videoTagListReq.getKeyword()),
                        VideoTagDO::getName,videoTagListReq.getKeyword())
                .or()
                .like(StringUtils.hasText(videoTagListReq.getKeyword()),
                        VideoTagDO::getTagList,videoTagListReq.getKeyword())
                .orderByDesc(VideoTagDO::getCreateTime);
        Page<VideoTagDO> mpPage = new Page<>(videoTagListReq.getPageNo(), videoTagListReq.getPageSize());
        Page<VideoTagDO> videoTagDOPage = this.selectPage(mpPage, videoTagDOLambdaQueryWrapper);
        return new PageResult<>(videoTagDOPage.getRecords(),videoTagDOPage.getTotal());
    }

}
