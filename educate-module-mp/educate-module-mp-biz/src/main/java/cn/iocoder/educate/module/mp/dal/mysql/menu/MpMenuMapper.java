package cn.iocoder.educate.module.mp.dal.mysql.menu;

import cn.iocoder.educate.module.mp.dal.dataobject.menu.MpMenuDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/5 16:26
 */
@Mapper
public interface MpMenuMapper extends BaseMapper<MpMenuDO> {

    default void deleteByAccountId(Long accountId) {
        LambdaQueryWrapper<MpMenuDO> mpMenuDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mpMenuDOLambdaQueryWrapper.eq(MpMenuDO::getAccountId,accountId);
        this.delete(mpMenuDOLambdaQueryWrapper);
    }

    default List<MpMenuDO> selectListByAccountId(Long accountId){
        LambdaQueryWrapper<MpMenuDO> mpMenuDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mpMenuDOLambdaQueryWrapper.eq(MpMenuDO::getAccountId, accountId);
        return this.selectList(mpMenuDOLambdaQueryWrapper);
    }

}
