package cn.iocoder.educate.module.infra.dal.mysql.logger;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.infra.controller.admin.logger.vo.apiaccesslog.ApiAccessLogPageReqVO;
import cn.iocoder.educate.module.infra.dal.dataobject.file.FileConfigDO;
import cn.iocoder.educate.module.infra.dal.dataobject.logger.ApiAccessLogDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/4 11:24
 */
@Mapper
public interface ApiAccessLogMapper extends BaseMapper<ApiAccessLogDO> {

    default PageResult<ApiAccessLogDO> selectPage(ApiAccessLogPageReqVO apiAccessLogPageReqVO) {
        LambdaQueryWrapper<ApiAccessLogDO> apiAccessLogDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apiAccessLogDOLambdaQueryWrapper
                .like(StringUtils.hasText(apiAccessLogPageReqVO.getRequestUrl()),
                        ApiAccessLogDO::getRequestUrl,apiAccessLogPageReqVO.getRequestUrl())
                .eq(ObjectUtil.isNotEmpty(apiAccessLogPageReqVO.getUserId()),
                        ApiAccessLogDO::getUserId,apiAccessLogPageReqVO.getUserId())
                .eq(ObjectUtil.isNotEmpty(apiAccessLogPageReqVO.getUserType()),
                        ApiAccessLogDO::getUserType,apiAccessLogPageReqVO.getUserType())
                .eq(ObjectUtil.isNotEmpty(apiAccessLogPageReqVO.getApplicationName()),
                        ApiAccessLogDO::getApplicationName,apiAccessLogPageReqVO.getApplicationName())
                .eq(ObjectUtil.isNotEmpty(apiAccessLogPageReqVO.getResultCode()),
                        ApiAccessLogDO::getResultCode,apiAccessLogPageReqVO.getResultCode())
                .eq(ObjectUtil.isNotEmpty(apiAccessLogPageReqVO.getUserId()),
                        ApiAccessLogDO::getUserId,apiAccessLogPageReqVO.getUserId())
                .between(null != ArrayUtils.get(apiAccessLogPageReqVO.getBeginTime(),0) &&
                                ArrayUtils.get(apiAccessLogPageReqVO.getBeginTime(),1) != null,
                        ApiAccessLogDO::getBeginTime,
                        ArrayUtils.get(apiAccessLogPageReqVO.getBeginTime(),0),
                        ArrayUtils.get(apiAccessLogPageReqVO.getBeginTime(),1))
                .ge(apiAccessLogPageReqVO.getDuration() != null,
                        ApiAccessLogDO::getDuration,apiAccessLogPageReqVO.getDuration())
                .orderByDesc(ApiAccessLogDO::getId);
        Page<ApiAccessLogDO> page = new Page<>(apiAccessLogPageReqVO.getPageNo(), apiAccessLogPageReqVO.getPageSize());
        Page<ApiAccessLogDO> apiAccessLogDOPage = this.selectPage(page, apiAccessLogDOLambdaQueryWrapper);
        return new PageResult<>(apiAccessLogDOPage.getRecords(),apiAccessLogDOPage.getTotal());
    }

}
