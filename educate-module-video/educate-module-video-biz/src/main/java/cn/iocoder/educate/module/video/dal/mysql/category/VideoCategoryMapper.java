package cn.iocoder.educate.module.video.dal.mysql.category;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.module.video.dal.dataobject.category.VideoCategoryDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/22 12:29
 */
@Mapper
public interface VideoCategoryMapper extends BaseMapper<VideoCategoryDO> {

    default List<VideoCategoryDO> getParentIdList(){
        LambdaQueryWrapper<VideoCategoryDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(VideoCategoryDO::getParentId, 0)
                .orderByAsc(VideoCategoryDO::getSortNum);
        return this.selectList(lambdaQueryWrapper);
    }

    default List<VideoCategoryDO> getParentIdList(Integer parentId){
        LambdaQueryWrapper<VideoCategoryDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(VideoCategoryDO::getParentId, parentId)
                .orderByAsc(VideoCategoryDO::getSortNum);
        return this.selectList(lambdaQueryWrapper);
    }

}
