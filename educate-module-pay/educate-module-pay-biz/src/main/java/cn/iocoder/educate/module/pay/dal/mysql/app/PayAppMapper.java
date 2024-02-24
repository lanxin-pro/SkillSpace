package cn.iocoder.educate.module.pay.dal.mysql.app;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.educate.module.pay.controller.admin.app.vo.PayAppPageReqVO;
import cn.iocoder.educate.module.pay.dal.dataobject.app.PayAppDO;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * @author j-sentinel
 * @date 2024/2/22 11:08
 */
@Mapper
public interface PayAppMapper extends BaseMapper<PayAppDO> {

    default PageResult<PayAppDO> selectPage(PayAppPageReqVO payAppPageReqVO) {
        LambdaQueryWrapper<PayAppDO> payAppDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        payAppDOLambdaQueryWrapper
                .like(StringUtils.hasText(payAppPageReqVO.getName()),
                        PayAppDO::getName,payAppPageReqVO.getName())
                .eq(ObjUtil.isNotEmpty(payAppPageReqVO.getStatus()),
                        PayAppDO::getStatus,payAppPageReqVO.getStatus())
                // 先根据type排序，然后根据sort排序
                .orderByDesc(PayAppDO::getId);
        Page<PayAppDO> page = new Page<>(payAppPageReqVO.getPageNo(), payAppPageReqVO.getPageSize());
        Page<PayAppDO> payAppDOPage = this.selectPage(page, payAppDOLambdaQueryWrapper);
        return new PageResult<>(payAppDOPage.getRecords(),payAppDOPage.getTotal());
    }

}
