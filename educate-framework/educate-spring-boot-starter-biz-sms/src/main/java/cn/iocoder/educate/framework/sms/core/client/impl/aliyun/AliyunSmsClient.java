package cn.iocoder.educate.framework.sms.core.client.impl.aliyun;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.core.KeyValue;
import cn.iocoder.educate.framework.common.util.collection.MapUtils;
import cn.iocoder.educate.framework.common.util.json.JsonUtils;
import cn.iocoder.educate.framework.sms.core.client.SmsCodeMapping;
import cn.iocoder.educate.framework.sms.core.client.SmsCommonResult;
import cn.iocoder.educate.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.educate.framework.sms.core.client.impl.AbstractSmsClient;
import cn.iocoder.educate.framework.sms.core.properties.SmsChannelProperties;
import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 阿里短信客户端的实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/5/29 19:57
 */
@Slf4j
public class AliyunSmsClient extends AbstractSmsClient {

    /**
     * REGION, 使用杭州
     */
    private static final String ENDPOINT = "cn-hangzhou";

    /**
     * 阿里云客户端
     */
    private volatile IAcsClient client;

    /**
     * {@link cn.iocoder.educate.framework.sms.core.client.impl.SmsClientFactoryImpl#createSmsClient()} 从中的属性赋值
     * @param smsChannelProperties
     */
    public AliyunSmsClient(SmsChannelProperties smsChannelProperties) {
        super(smsChannelProperties, new AliyunSmsCodeMapping());
        Assert.notEmpty(smsChannelProperties.getApiKey(), "apiKey 不能为空");
        Assert.notEmpty(smsChannelProperties.getApiSecret(), "apiSecret 不能为空");
    }

    @Override
    protected SmsCommonResult<SmsSendRespDTO> doSendSms(Long sendLogId, String mobile, String apiTemplateId, List<KeyValue<String, Object>> templateParams) throws Throwable {
        // 构建参数
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(mobile);
        request.setSignName(properties.getSignature());
        request.setTemplateCode(apiTemplateId);
        request.setTemplateParam(JsonUtils.toJsonString(MapUtils.convertMap(templateParams)));
        request.setOutId(String.valueOf(sendLogId));
        // 执行请求
        return invoke(request, response -> new SmsSendRespDTO().setSerialNo(response.getBizId()));
    }

    /**
     * 这四个是核心字段
     * PhoneNumbers接收短信的手机号码
     * SignName短信签名名称
     * TemplateCode短信模板CODE
     * TemplateParam短信模板变量对应的实际值
     * @param request
     * @param responseConsumer
     * @param <T>
     * @param <R>
     * @return
     */
    private <T extends AcsResponse, R> SmsCommonResult<R> invoke(AcsRequest<T> request, Function<T, R> responseConsumer) {
        try {
            // 执行发送. 由于阿里云 sms 短信没有统一的 Response，但是有统一的 code、message、requestId 属性，所以只好反射
            T sendResult = client.getAcsResponse(request);
            String code = (String) ReflectUtil.getFieldValue(sendResult, "code");
            String message = (String) ReflectUtil.getFieldValue(sendResult, "message");
            String requestId = (String) ReflectUtil.getFieldValue(sendResult, "requestId");
            // 解析结果
            R data = null;
            if (Objects.equals(code, "OK")) { // 请求成功的情况下
                data = responseConsumer.apply(sendResult);
            }
            // 拼接结果
            return SmsCommonResult.build(code, message, requestId, data, codeMapping);
        } catch (ClientException ex) {
            return SmsCommonResult.build(ex.getErrCode(), formatResultMsg(ex), ex.getRequestId(), null, codeMapping);
        }
    }

    @Override
    protected void doInit() {
        IClientProfile profile = DefaultProfile.getProfile(ENDPOINT, properties.getApiKey(), properties.getApiSecret());
        client = new DefaultAcsClient(profile);
    }

    private static String formatResultMsg(ClientException ex) {
        if (StrUtil.isEmpty(ex.getErrorDescription())) {
            return ex.getErrMsg();
        }
        return ex.getErrMsg() + " => " + ex.getErrorDescription();
    }
}
