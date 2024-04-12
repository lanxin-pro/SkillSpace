package cn.iocoder.educate.module.pay.dal.mysql.demo;

import cn.iocoder.educate.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.educate.module.pay.dal.dataobject.demo.PayDemoTransferDO;
import cn.iocoder.educate.module.pay.service.demo.PayDemoTransferService;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author j-sentinel
 * @date 2024/4/5 16:52
 */
@Mapper
public interface PayDemoTransferMapper extends BaseMapperX<PayDemoTransferDO> {
}
