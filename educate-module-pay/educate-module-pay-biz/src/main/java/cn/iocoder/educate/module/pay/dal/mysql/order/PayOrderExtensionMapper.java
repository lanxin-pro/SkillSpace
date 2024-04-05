package cn.iocoder.educate.module.pay.dal.mysql.order;

import cn.iocoder.educate.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.educate.module.pay.dal.dataobject.order.PayOrderExtensionDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author j-sentinel
 * @date 2024/3/27 21:45
 */
@Mapper
public interface PayOrderExtensionMapper extends BaseMapper<PayOrderExtensionDO> {
}
