package cn.iocoder.educate.module.video.dal.mysql;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminPageReqVO;
import cn.iocoder.educate.module.video.dal.dataobject.VideoAdminDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/27 14:01
 */
@Mapper
public interface VideoAdminMapper extends BaseMapper<VideoAdminDO> {

    default PageResult<VideoAdminDO> selectPage(VideoAdminPageReqVO videoPageReqVO){
        LambdaQueryWrapper<VideoAdminDO> videoAdminDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 查询放入到一个地方
        if (ObjectUtil.isNotEmpty(videoPageReqVO.getKeyword())) {
            videoAdminDOLambdaQueryWrapper.and(c -> c.like(VideoAdminDO::getTitle, videoPageReqVO.getKeyword())
                    .or().like(VideoAdminDO::getVideoCode, videoPageReqVO.getKeyword())
                    .or().like(VideoAdminDO::getActorNames, videoPageReqVO.getKeyword())
                    .or().like(VideoAdminDO::getTagList, videoPageReqVO.getKeyword()));
        }
        videoAdminDOLambdaQueryWrapper
                .eq(ObjectUtil.isNotEmpty(videoPageReqVO.getEnableStatus()),
                        VideoAdminDO::getEnableStatus,videoPageReqVO.getEnableStatus())
                .eq(ObjectUtil.isNotEmpty(videoPageReqVO.getContentType()),
                        VideoAdminDO::getContentType,videoPageReqVO.getContentType())
                .eq(ObjectUtil.isNotEmpty(videoPageReqVO.getRegion()),
                        VideoAdminDO::getRegion,videoPageReqVO.getRegion())
                .eq(ObjectUtil.isNotEmpty(videoPageReqVO.getMosaicFlag()),
                        VideoAdminDO::getMosaicFlag,videoPageReqVO.getMosaicFlag())
                .eq(ObjectUtil.isNotEmpty(videoPageReqVO.getSubtitleFlag()),
                        VideoAdminDO::getSubtitleFlag,videoPageReqVO.getSubtitleFlag())
                .eq(ObjectUtil.isNotEmpty(videoPageReqVO.getPriceType()),
                        VideoAdminDO::getPriceType,videoPageReqVO.getPriceType())
                .eq(ObjectUtil.isNotEmpty(videoPageReqVO.getDatasource()),
                        VideoAdminDO::getDatasource,videoPageReqVO.getDatasource())
                /*一级分类查询*/
                .apply(StrUtil.isNotEmpty(videoPageReqVO.getCategoryPid()),
                    "FIND_IN_SET({0},category_pid)", videoPageReqVO.getCategoryPid())
                /*二级分类查询*/
                .apply(StrUtil.isNotEmpty(videoPageReqVO.getCategoryId()),
                    "FIND_IN_SET({0},category_id)", videoPageReqVO.getCategoryId())
                .between(null != ArrayUtils.get(videoPageReqVO.getCreateTime(),0) &&
                                ArrayUtils.get(videoPageReqVO.getCreateTime(),1) != null,
                        VideoAdminDO::getCreateTime, ArrayUtils.get(videoPageReqVO.getCreateTime(),0),
                        ArrayUtils.get(videoPageReqVO.getCreateTime(),1))
                .orderByDesc(VideoAdminDO::getCreateTime);
        Page<VideoAdminDO> mpPage = new Page<>(videoPageReqVO.getPageNo(), videoPageReqVO.getPageSize());
        Page<VideoAdminDO> adminUserDOPage = this.selectPage(mpPage, videoAdminDOLambdaQueryWrapper);
        return new PageResult<>(adminUserDOPage.getRecords(),adminUserDOPage.getTotal());
    }

}
