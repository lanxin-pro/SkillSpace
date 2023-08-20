package cn.iocoder.educate.module.system.dal.mysql.errorcode;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.system.controller.admin.errorcode.vo.ErrorCodePageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.errorcode.ErrorCodeDO;
import cn.iocoder.educate.module.system.dal.dataobject.mail.MailTemplateDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/20 10:37
 */
@Mapper
public interface ErrorCodeMapper extends BaseMapper<ErrorCodeDO> {

    default ErrorCodeDO selectByCode(Integer code){
        LambdaQueryWrapper<ErrorCodeDO> errorCodeDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        errorCodeDOLambdaQueryWrapper.eq(ErrorCodeDO::getCode,code);
        return this.selectOne(errorCodeDOLambdaQueryWrapper);
    }

    default PageResult<ErrorCodeDO> selectPage(ErrorCodePageReqVO errorCodePageReqVO) {
        LambdaQueryWrapper<ErrorCodeDO> errorCodeDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        errorCodeDOLambdaQueryWrapper
                .like(StringUtils.hasText(errorCodePageReqVO.getApplicationName()),
                        ErrorCodeDO::getApplicationName,errorCodePageReqVO.getApplicationName())
                .like(StringUtils.hasText(errorCodePageReqVO.getMessage()),
                        ErrorCodeDO::getMessage,errorCodePageReqVO.getMessage())
                .eq(ObjectUtil.isNotEmpty(errorCodePageReqVO.getType()),
                        ErrorCodeDO::getType,errorCodePageReqVO.getType())
                .eq(ObjectUtil.isNotEmpty(errorCodePageReqVO.getCode()),
                        ErrorCodeDO::getCode,errorCodePageReqVO.getCode())
                .between(null != ArrayUtils.get(errorCodePageReqVO.getCreateTime(),0) &&
                                ArrayUtils.get(errorCodePageReqVO.getCreateTime(),1) != null,
                        ErrorCodeDO::getCreateTime,
                        ArrayUtils.get(errorCodePageReqVO.getCreateTime(),0),
                        ArrayUtils.get(errorCodePageReqVO.getCreateTime(),1))
                .orderByDesc(ErrorCodeDO::getCode);
        Page<ErrorCodeDO> page = new Page<>(errorCodePageReqVO.getPageNo(), errorCodePageReqVO.getPageSize());
        Page<ErrorCodeDO> errorCodeDOPage = this.selectPage(page, errorCodeDOLambdaQueryWrapper);
        return new PageResult<>(errorCodeDOPage.getRecords(),errorCodeDOPage.getTotal());
    }

    default List<ErrorCodeDO> selectListByCodes(Set<Integer> codes){
        LambdaQueryWrapper<ErrorCodeDO> errorCodeDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        errorCodeDOLambdaQueryWrapper.in(ErrorCodeDO::getCode,codes);
        return this.selectList(errorCodeDOLambdaQueryWrapper);
    }

}
