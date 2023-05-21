package cn.iocoder.educate.module.system.service.sms;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ServiceLoaderUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.util.servlet.ServletUtils;
import cn.iocoder.educate.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsCodeDo;
import cn.iocoder.educate.module.system.dal.mysql.sms.SmsCodeMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.enums.sms.SmsSceneEnum;
import cn.iocoder.educate.module.system.framework.sms.SmsCodeProperties;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 短信验证码 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/5/19 11:43
 */
@Service
@Validated
public class SmsCodeServiceImpl implements SmsCodeService{

    @Resource
    private SmsCodeProperties smsCodeProperties;

    @Resource
    private SmsSendService smsSendService;

    @Resource
    private SmsCodeMapper smsCodeMapper;

    @Override
    public void sendSmsCode(SmsCodeSendReqDTO reqDTO) {
        // 获取短信的配置信息
        SmsSceneEnum codeByScene = SmsSceneEnum.getCodeByScene(reqDTO.getScene());
        Assert.notNull(codeByScene, "验证码场景({}) 查找不到配置", reqDTO.getScene());
        // 创建验证码
        String code = createSmsCode(reqDTO.getMobile(), reqDTO.getScene(), reqDTO.getCreateIp());
        // 发送验证码
        smsSendService.sendSingleSms(reqDTO.getMobile(), null, null,
                codeByScene.getTemplateCode(), MapUtil.of("code", code));
    }

    /**
     * 创建验证码给三个参数
     * @param mobile
     * @param scene 发送验证码的场景
     * @param createIp
     * @return
     */
    private String createSmsCode(String mobile, Integer scene, String createIp) {
        // 一定获取的是今天最后的一个验证码奥（校验是否可以发送验证码，不用筛选场景）
        SmsCodeDo lastSmsCode = smsCodeMapper.selectLastByMobile(mobile, null, null);
        if(ObjUtil.isNotEmpty(lastSmsCode)){
            // 计算出当前时间与上一次短信验证码发送时间的时间差 time
            long time = LocalDateTimeUtil.between(lastSmsCode.getCreateTime(), LocalDateTime.now()).toMillis();
            // 从配置文件中获取设置的短信发送频率
            long textingFrequency = smsCodeProperties.getSendFrequency().toMillis();
            // 判断时间差是否小于短信发送频率   也就是说在一个时间段之内我能发送多少频率
            if(time < textingFrequency){
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_CODE_SEND_TOO_FAST);
            }
            boolean sameDay = LocalDateTimeUtil.isSameDay(lastSmsCode.getCreateTime(), LocalDateTime.now());
            // 必须是今天才能计算发送短信的频率，超过每日短信发送数量
            if(sameDay && lastSmsCode.getTodayIndex() >= smsCodeProperties.getSendMaximumQuantityPerDay()){
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_CODE_EXCEED_SEND_MAXIMUM_QUANTITY_PER_DAY);
            }
            // 设置同一个ip时间段固定发送多少条
            String lastIp = lastSmsCode.getCreateIp();
            if(sameDay && ServletUtils.getClientIP().equals(lastIp)  && lastSmsCode.getIpIndex() >= smsCodeProperties.getSendMaximumQuantityPerIp()){
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_CODE_SEND_TOO_FAST);
            }
        }
        // 创建验证码记录
        int iCode = RandomUtil.randomInt(smsCodeProperties.getBeginCode(), smsCodeProperties.getEndCode());
        String sCode = String.valueOf(iCode);
        // 是否是今天
        SmsCodeDo newSmsCode = SmsCodeDo.builder()
                .mobile(mobile)
                .code(sCode)
                .scene(scene)
                // 如果是今天的话，那么就给今天的记录+1，如果不是今天的话，那么就代表没有发起验证码过，那么就等于1
                .todayIndex(
                        ObjUtil.isNotEmpty(lastSmsCode)
                        &&
                        LocalDateTimeUtil.isSameDay(lastSmsCode.getCreateTime(), LocalDateTime.now())
                        ?
                        lastSmsCode.getTodayIndex() + 1 : 1)
                // 设置今日发送的ip数量
                .ipIndex(
                        ObjUtil.isNotEmpty(lastSmsCode)
                        &&
                        LocalDateTimeUtil.isSameDay(lastSmsCode.getCreateTime(), LocalDateTime.now())
                        ?
                        lastSmsCode.getIpIndex() + 1 : 1)
                .createIp(createIp)
                .used(false).build();
        smsCodeMapper.insert(newSmsCode);
        return sCode;
    }
}
