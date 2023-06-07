package cn.iocoder.educate.framework.sms.core.client.impl.aliyun;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.iocoder.educate.framework.common.core.KeyValue;
import cn.iocoder.educate.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.educate.framework.common.util.collection.MapUtils;
import cn.iocoder.educate.framework.common.util.json.JsonUtils;
import cn.iocoder.educate.framework.sms.core.client.SmsCommonResult;
import cn.iocoder.educate.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.educate.framework.sms.core.properties.SmsChannelProperties;
import cn.iocoder.educate.framework.test.core.ut.BaseMockitoUnitTest;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

/**
 * {@link AliyunSmsClient} 的单元测试
 *
 * @Author: j-sentinel
 * @Date: 2023/6/6 10:08
 */
public class AliyunSmsClientTest extends BaseMockitoUnitTest {

    private final SmsChannelProperties properties = new SmsChannelProperties()
            .setApiKey(randomString()) // 随机一个 apiKey，避免构建报错
            .setApiSecret(randomString()) // 随机一个 apiSecret，避免构建报错
            .setSignature("纳希妲");

    @InjectMocks
    private final AliyunSmsClient smsClient = new AliyunSmsClient(properties);

    @Mock
    private IAcsClient client;

    @Test
    public void testDoInit() {
        // 准备参数
        // mock 方法

        // 调用
        smsClient.doInit();
        // 断言
        Assertions.assertNotSame(client, ReflectUtil.getFieldValue(smsClient, "acsClient"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDoSendSms() throws ClientException {
        // 准备参数
        Long sendLogId = RandomUtil.randomLong(0,Long.MAX_VALUE);
        String mobile = randomString();
        String apiTemplateId = randomString();
        List<KeyValue<String, Object>> templateParams = Lists.newArrayList(
                new KeyValue<>("code", 1234), new KeyValue<>("op", "login"));
        // mock 方法
        SendSmsResponse response = randomPojo(SendSmsResponse.class, o -> o.setCode("OK"));
        when(client.getAcsResponse(argThat((ArgumentMatcher<SendSmsRequest>) acsRequest -> {
            assertEquals(mobile, acsRequest.getPhoneNumbers());
            assertEquals(properties.getSignature(), acsRequest.getSignName());
            assertEquals(apiTemplateId, acsRequest.getTemplateCode());
            assertEquals(JsonUtils.toJsonString(MapUtils.convertMap(templateParams)), acsRequest.getTemplateParam());
            assertEquals(sendLogId.toString(), acsRequest.getOutId());
            return true;
        }))).thenReturn(response);

        // 调用
        SmsCommonResult<SmsSendRespDTO> result = smsClient.doSendSms(sendLogId, mobile,
                apiTemplateId, templateParams);
        // 断言
        assertEquals(response.getCode(), result.getApiCode());
        assertEquals(response.getMessage(), result.getApiMsg());
        assertEquals(GlobalErrorCodeConstants.SUCCESS.getCode(), result.getCode());
        assertEquals(GlobalErrorCodeConstants.SUCCESS.getMsg(), result.getMsg());
        assertEquals(response.getRequestId(), result.getApiRequestId());
        // 断言结果
        assertEquals(response.getBizId(), result.getData().getSerialNo());
    }

    @SafeVarargs
    public static <T> T randomPojo(Class<T> clazz, Consumer<T>... consumers) {
        T pojo = new PodamFactoryImpl().manufacturePojo(clazz);
        // 非空时，回调逻辑。通过它，可以实现 Pojo 的进一步处理
        if (ArrayUtil.isNotEmpty(consumers)) {
            Arrays.stream(consumers).forEach(consumer -> consumer.accept(pojo));
        }
        return pojo;
    }

    public static String randomString() {
        return RandomUtil.randomString(10);
    }
}
