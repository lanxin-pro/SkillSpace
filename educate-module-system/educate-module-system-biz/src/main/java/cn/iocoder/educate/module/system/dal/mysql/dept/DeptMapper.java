package cn.iocoder.educate.module.system.dal.mysql.dept;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/3 17:40
 */
@Mapper
public interface DeptMapper extends BaseMapper<DeptDO> {

    default List<DeptDO> selectList(DeptListReqVO reqVO) {
        LambdaQueryWrapper<DeptDO> deptDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        deptDOLambdaQueryWrapper.like(StringUtils.hasText(reqVO.getName()),DeptDO::getName,reqVO.getName());
        deptDOLambdaQueryWrapper.eq(ObjectUtil.isNotEmpty(reqVO.getStatus()),DeptDO::getStatus,reqVO.getStatus());
        return this.selectList(deptDOLambdaQueryWrapper);
    }
}
