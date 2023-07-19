package cn.iocoder.educate.module.system.dal.mysql.permission;

import cn.iocoder.educate.module.system.controller.admin.permission.vo.menu.MenuListReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.MenuDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/6/23 17:35
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuDO> {

    default List<MenuDO> selectList(MenuListReqVO reqVO) {
        LambdaQueryWrapper<MenuDO> menuDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuDOLambdaQueryWrapper.like(StringUtils.hasText(reqVO.getName()),MenuDO::getName,reqVO.getName());
        menuDOLambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(reqVO.getStatus()),MenuDO::getStatus,reqVO.getStatus());
        return this.selectList(menuDOLambdaQueryWrapper);
    }

}
