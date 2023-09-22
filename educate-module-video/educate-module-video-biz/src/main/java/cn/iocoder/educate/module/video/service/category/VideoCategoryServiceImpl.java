package cn.iocoder.educate.module.video.service.category;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.module.video.controller.admin.category.vo.category.VideoCategoryRespVO;
import cn.iocoder.educate.module.video.covert.category.VideoCategoryConvert;
import cn.iocoder.educate.module.video.dal.dataobject.category.VideoCategoryDO;
import cn.iocoder.educate.module.video.dal.mysql.category.VideoCategoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 视频分类service实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/9/22 12:24
 */
@Slf4j
@Service
public class VideoCategoryServiceImpl implements VideoCategoryService {

    @Resource
    private VideoCategoryMapper videoCategoryMapper;

    @Override
    public List<VideoCategoryRespVO> findVideoCategoriesTree() {
        // 全部父类分类
        List<VideoCategoryDO> parentIdList = videoCategoryMapper.getParentIdList();
        List<VideoCategoryRespVO> videoCategoryRespVOList = new ArrayList<>();
        // 全部分类
        List<VideoCategoryDO> pageInfoList = videoCategoryMapper.selectList(new LambdaQueryWrapper<>());

        if (CollUtil.isNotEmpty(parentIdList)) {
            // 循环放入到VO中
            videoCategoryRespVOList = pageInfoList.stream().map(categoryDO -> {
                VideoCategoryRespVO videoCategoryVO = VideoCategoryConvert.INSTANCE.covert(categoryDO);
                // 查询所以的子分类
                videoCategoryVO.setChildrenList(findCildrens(categoryDO.getId()));
                return videoCategoryVO;
            }).collect(Collectors.toList());
        }
        return videoCategoryRespVOList;
    }


    public List<VideoCategoryRespVO> findCildrens(Integer parentId) {
        List<VideoCategoryDO> videoCategoryDOS = videoCategoryMapper.getParentIdList(parentId);
        // 如果是 parentId是0 那么就是空
        if (CollectionUtils.isEmpty(videoCategoryDOS)) {
            videoCategoryDOS = new ArrayList<>();
        }
        return VideoCategoryConvert.INSTANCE.covertList(videoCategoryDOS);
    }

}
