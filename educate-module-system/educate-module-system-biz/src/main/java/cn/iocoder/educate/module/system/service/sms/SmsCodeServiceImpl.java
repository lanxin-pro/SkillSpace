package cn.iocoder.educate.module.system.service.sms;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.util.servlet.ServletUtils;
import cn.iocoder.educate.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import cn.iocoder.educate.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import cn.iocoder.educate.module.system.dal.dataobject.sms.SmsCodeDo;
import cn.iocoder.educate.module.system.dal.mysql.sms.SmsCodeMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.enums.sms.SmsSceneEnum;
import cn.iocoder.educate.module.system.framework.sms.SmsCodeProperties;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 短信验证码 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/5/19 11:43
 */
@Service
@Validated
public class SmsCodeServiceImpl implements SmsCodeService{

    private static final Integer VAR_ONE_HOUR = 1;

    @Resource
    private SmsCodeProperties smsCodeProperties;

    @Resource
    private SmsSendService smsSendService;

    @Resource
    private SmsCodeMapper smsCodeMapper;

    @Override
    public void sendSmsCode(SmsCodeSendReqDTO reqDTO) {
        // 获取短信的配置信息 @InEnum(SmsSceneEnum.class) 可以根据Integer获取当前枚举的全部信息
        SmsSceneEnum codeByScene = SmsSceneEnum.getCodeByScene(reqDTO.getScene());
        Assert.notNull(codeByScene, "验证码场景({}) 查找不到配置", reqDTO.getScene());
        // 创建验证码
        String code = createSmsCode(reqDTO.getMobile(), reqDTO.getScene(), reqDTO.getCreateIp());
        // 发送验证码
        smsSendService.sendSingleSms(reqDTO.getMobile(), null, null,
                // 快速的创建一个键值对(看hutu源码)
                codeByScene.getTemplateCode(), MapUtil.of("code", code));
    }

    @Override
    public void useSmsCode(SmsCodeUseReqDTO smsCodeUseReqDTO) {
        // 检测验证码是否有效
        SmsCodeDo lastSmsCodeDo = validateSmsCode0(smsCodeUseReqDTO.getMobile(),
                smsCodeUseReqDTO.getCode(), smsCodeUseReqDTO.getScene());
        if(lastSmsCodeDo == null){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_CODE_NOT_CORRECT);
        }
        smsCodeMapper.updateById(SmsCodeDo.builder()
                .id(lastSmsCodeDo.getId())
                .used(true)
                .usedTime(LocalDateTime.now())
                .usedIp(smsCodeUseReqDTO.getUsedIp())
                .build()
        );
    }

    private SmsCodeDo validateSmsCode0(String mobile, String code, Integer scene) {
        // 校验验证码
        SmsCodeDo lastSmsCodeDo = smsCodeMapper.selectLastByMobile(mobile, code, scene);
        // 若验证码不存在，抛出异常
        if(lastSmsCodeDo == null){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_CODE_NOT_FOUND);
        }
        // 超过时间
        if(LocalDateTimeUtil.between(lastSmsCodeDo.getCreateTime(),LocalDateTimeUtil.now()).toMillis()
            >= smsCodeProperties.getExpireTimes().toMillis()){ // 验证码过期了
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_CODE_EXPIRED);
        }
        if(Boolean.TRUE.equals(lastSmsCodeDo.getUsed())){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_CODE_USED);
        }
        return lastSmsCodeDo;
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
            // 是否当前1小时
            boolean sameHour = LocalDateTimeUtil.isIn(LocalDateTime.now(),lastSmsCode.getCreateTime(),
                    LocalDateTimeUtil.offset(lastSmsCode.getCreateTime(),VAR_ONE_HOUR, ChronoUnit.HOURS));
            // 设置同一个ip时间段固定发送多少条
            String lastIp = lastSmsCode.getCreateIp();
            if(sameHour && ServletUtils.getClientIP().equals(lastIp)  && lastSmsCode.getIpIndex() >= smsCodeProperties.getSendMaximumQuantityPerIp()){
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.SMS_CODE_EXCEED_SEND_IP_QUANTITY_PER_HOUR);
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
                // 设置每小时发送的ip数量
                // TODO j-sentinel 这里有一些BUG，就是我这个IP他是一种续期的逻辑
                .ipIndex(
                        ObjUtil.isNotEmpty(lastSmsCode)
                        &&
                        LocalDateTimeUtil.isIn(LocalDateTime.now(),lastSmsCode.getCreateTime(),
                            LocalDateTimeUtil.offset(lastSmsCode.getCreateTime(),VAR_ONE_HOUR, ChronoUnit.HOURS))
                        ?
                        lastSmsCode.getIpIndex() + 1 : 1)
                .createIp(createIp)
                .used(false).build();
        smsCodeMapper.insert(newSmsCode);
        return sCode;
    }
}
