package cn.iocoder.educate.module.system.dal.mysql.sms;

import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsTemplateDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/27 20:32
 */
@Mapper
public interface SmsTemplateMapper extends BaseMapper<SmsTemplateDO> {
}
