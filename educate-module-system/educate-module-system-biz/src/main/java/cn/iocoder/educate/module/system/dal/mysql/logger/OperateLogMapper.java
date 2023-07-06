package cn.iocoder.educate.module.system.dal.mysql.logger;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.system.controller.admin.logger.vo.OperateLogPageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.logger.OperateLogDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 15:09
 */
@Mapper
public interface OperateLogMapper extends BaseMapper<OperateLogDO> {

    default PageResult<OperateLogDO> selectPage(OperateLogPageReqVO reqVO, Collection<Long> userIds) {
        LambdaQueryWrapper<OperateLogDO> operateLogDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        operateLogDOLambdaQueryWrapper
                .like(StringUtils.hasText(reqVO.getModule()),OperateLogDO::getModule,reqVO.getModule())
                // ids根据nickName查询出来的
                .in(CollectionUtil.isNotEmpty(userIds),OperateLogDO::getUserId,userIds)
                .eq(ObjectUtil.isNotEmpty(reqVO.getType()),OperateLogDO::getType,reqVO.getType())
                .between(ArrayUtils.get(reqVO.getStartTime(),0) != null
                                && ArrayUtils.get(reqVO.getStartTime(),1) != null,
                        OperateLogDO::getStartTime,
                        ArrayUtils.get(reqVO.getStartTime(),0),
                        ArrayUtils.get(reqVO.getStartTime(),1))
                .ge(ArrayUtils.get(reqVO.getStartTime(),0) !=
                                null,
                        OperateLogDO::getStartTime,
                        ArrayUtils.get(reqVO.getStartTime(),0))
                .le(ArrayUtils.get(reqVO.getStartTime(),1) !=
                                null,
                        OperateLogDO::getStartTime,
                        ArrayUtils.get(reqVO.getStartTime(),1));
        if(Boolean.TRUE.equals(reqVO.getSuccess())){
            operateLogDOLambdaQueryWrapper.eq(OperateLogDO::getResultCode, GlobalErrorCodeConstants.SUCCESS.getCode());
        }else if (Boolean.FALSE.equals(reqVO.getSuccess())) {
            operateLogDOLambdaQueryWrapper.gt(OperateLogDO::getResultCode, GlobalErrorCodeConstants.SUCCESS.getCode());
        }
        // 降序
        operateLogDOLambdaQueryWrapper.orderByDesc(OperateLogDO::getId);
        Page<OperateLogDO> operateLogDOPage = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        Page<OperateLogDO> operateLogDOPageData = this.selectPage(operateLogDOPage, operateLogDOLambdaQueryWrapper);
        return new PageResult<>(operateLogDOPageData.getRecords(),operateLogDOPageData.getTotal());
    }
}
