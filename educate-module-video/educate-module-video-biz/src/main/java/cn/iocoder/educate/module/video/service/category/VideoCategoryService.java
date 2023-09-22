package cn.iocoder.educate.module.video.service.category;

import cn.iocoder.educate.module.video.controller.admin.category.vo.VideoCategoryRespVO;

import java.util.List;

/**
 * 视频分类service
 *
 * @Author: j-sentinel
 * @Date: 2023/9/22 12:24
 */
public interface VideoCategoryService {

    /**
     * 查询视频分类形成父子结构
     */
    List<VideoCategoryRespVO> findVideoCategoriesTree();

}
