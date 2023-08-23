package cn.iocoder.educate.module.infra.dal.mysql.logger;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogPageReqVO;
import cn.iocoder.educate.module.infra.dal.dataobject.logger.ApiAccessLogDO;
import cn.iocoder.educate.module.infra.dal.dataobject.logger.ApiErrorLogDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/7 18:09
 * API 错误日志 Mapper
 */
@Mapper
public interface ApiErrorLogMapper extends BaseMapper<ApiErrorLogDO> {

    default PageResult<ApiErrorLogDO> selectPage(ApiErrorLogPageReqVO apiErrorLogPageReqVO) {
        LambdaQueryWrapper<ApiErrorLogDO> apiErrorLogDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apiErrorLogDOLambdaQueryWrapper
                .like(StringUtils.hasText(apiErrorLogPageReqVO.getRequestUrl()),
                        ApiErrorLogDO::getRequestUrl,apiErrorLogPageReqVO.getRequestUrl())
                .eq(ObjectUtil.isNotEmpty(apiErrorLogPageReqVO.getUserId()),
                        ApiErrorLogDO::getUserId,apiErrorLogPageReqVO.getUserId())
                .eq(ObjectUtil.isNotEmpty(apiErrorLogPageReqVO.getUserType()),
                        ApiErrorLogDO::getUserType,apiErrorLogPageReqVO.getUserType())
                .eq(ObjectUtil.isNotEmpty(apiErrorLogPageReqVO.getApplicationName()),
                        ApiErrorLogDO::getApplicationName,apiErrorLogPageReqVO.getApplicationName())
                .eq(ObjectUtil.isNotEmpty(apiErrorLogPageReqVO.getProcessStatus()),
                        ApiErrorLogDO::getProcessStatus,apiErrorLogPageReqVO.getProcessStatus())
                .between(null != ArrayUtils.get(apiErrorLogPageReqVO.getExceptionTime(),0) &&
                                ArrayUtils.get(apiErrorLogPageReqVO.getExceptionTime(),1) != null,
                        ApiErrorLogDO::getExceptionTime,
                        ArrayUtils.get(apiErrorLogPageReqVO.getExceptionTime(),0),
                        ArrayUtils.get(apiErrorLogPageReqVO.getExceptionTime(),1))
                .orderByDesc(ApiErrorLogDO::getId);
        Page<ApiErrorLogDO> page = new Page<>(apiErrorLogPageReqVO.getPageNo(), apiErrorLogPageReqVO.getPageSize());
        Page<ApiErrorLogDO> apiErrorLogDOPage = this.selectPage(page, apiErrorLogDOLambdaQueryWrapper);
        return new PageResult<>(apiErrorLogDOPage.getRecords(),apiErrorLogDOPage.getTotal());
    }
}
