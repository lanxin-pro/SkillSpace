package cn.iocoder.educate.module.system.dal.mysql.sms;

import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsCodeDo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/19 12:06
 */
@Mapper
public interface SmsCodeMapper extends BaseMapper<SmsCodeDo>{

    /**
     * 获得手机号今天的最后一个手机验证码
     * @param mobile 手机号
     * @param code 发送场景，选填
     * @param scene 验证码 选填
     * @return 手机验证码
     */
    default SmsCodeDo selectLastByMobile(String mobile, String code, Integer scene){
        QueryWrapper<SmsCodeDo> smsCodeDoQueryWrapper = new QueryWrapper<>();
        smsCodeDoQueryWrapper.eq("mobile",mobile);
        smsCodeDoQueryWrapper.eq(code != null,"code",code);
        smsCodeDoQueryWrapper.eq(scene != null,"scene",scene);
        smsCodeDoQueryWrapper.orderByDesc("id").last("LIMIT " + 1);
        return selectOne(smsCodeDoQueryWrapper);
    }
}
