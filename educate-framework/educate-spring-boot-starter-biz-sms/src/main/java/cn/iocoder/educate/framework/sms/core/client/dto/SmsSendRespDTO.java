package cn.iocoder.educate.framework.sms.core.client.dto;

import lombok.Data;

/**
 * 短信发送 Response DTO
 *
 * @Author: j-sentinel
 * @Date: 2023/6/3 17:50
 */
@Data
public class SmsSendRespDTO {

    /**
     * 短信 API 发送返回的序号
     */
    private String serialNo;
}
